package com.nthuy.demo_erm.service.impl;


import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.ReasonSpecification;
import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.entity.*;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.mapper.ReasonMapper;
import com.nthuy.demo_erm.proxy.SystemProxy;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.repository.ReasonMapRepository;
import com.nthuy.demo_erm.repository.ReasonRepository;
import com.nthuy.demo_erm.service.ReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository reasonRepository;
    private final ReasonMapper reasonMapper;
    private final ReasonMapRepository reasonMapRepository;
    private final SystemProxy systemProxy;
    private final ClassifyReasonRepository classifyReasonRepository;

    @Override
    @Transactional
    public Long create(ReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), null);
        this.validateCodeNotExists(dto.getCode(), null);
        ReasonEntity entity = reasonMapper.toEntity(dto);
        reasonRepository.save(entity);

        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));
        saveReasonSystemMap(entity.getId(), systemIds);
        return entity.getId();
    }

    @Override
    public ReasonDTO getReason(Long id) {

        ReasonEntity reason = this.reasonRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));
        ReasonDTO dto = reasonMapper.toDto(reason);
        dto.setSystems(getSystemsByReasonId(id));

        if (reason.getClassifyReasonId() != null) {
            ClassifyReasonEntity classify = classifyReasonRepository.findById(reason.getClassifyReasonId()).orElseThrow(() -> new BadRequestValidationException("ClassifyReason ID " + reason.getClassifyReasonId() + " không tồn tại"));
            dto.setClassifyReason(new ClassifyReasonResponse(classify.getId(), classify.getCode(), classify.getName()));
        }
        return dto;
    }

    @Override
    public void delete(Long id) {
        validateIdExists(id);
        reasonRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(ReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), dto.getId());
        this.validateCodeNotExists(dto.getCode(), dto.getId());

        ReasonEntity reason = reasonRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Nguyên nhân với ID " + dto.getId() + " không tồn tại"));
        reasonMapper.updateEntityFromDto(dto, reason);
        reasonRepository.save(reason);

        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));
        reasonMapRepository.deleteByReasonId(reason.getId());
        saveReasonSystemMap(reason.getId(), systemIds);

        return reason.getId();
    }

    @Override
    public ResultPaginationDTO<ReasonDTO> getListReason(String code, String name, List<Long> systemIds, Boolean isActive, EnumTypeReason type, Pageable pageable) {

        Specification<ReasonEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, code, ReasonSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, name, ReasonSpecification::hasName);
        spec = SpecificationUtils.addIfNotEmpty(spec, systemIds, ReasonSpecification::hasSystemIdIn);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, ReasonSpecification::hasIsActive);
        spec = SpecificationUtils.addIfNotNull(spec, type, ReasonSpecification::hasType);

        Page<ReasonEntity> pageResult = reasonRepository.findAll(spec, pageable);
        List<ReasonEntity> reasonEntities = pageResult.getContent();

        if (reasonEntities.isEmpty()) {
            return PaginationUtils.buildResult(pageResult, Collections.emptyList(), pageable);
        }

        List<ReasonDTO> dtoList = reasonMapper.toDtoList(reasonEntities);
        List<Long> reasonIds = reasonEntities.stream().map(ReasonEntity::getId).collect(Collectors.toList());

        // Batch query cho ClassifyReason
        Set<Long> classifyReasonIds = reasonEntities.stream()
                .map(ReasonEntity::getClassifyReasonId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, ClassifyReasonEntity> classifyReasonMap = classifyReasonIds.isEmpty()
                ? Collections.emptyMap()
                : classifyReasonRepository.findAllById(classifyReasonIds).stream()
                .collect(Collectors.toMap(ClassifyReasonEntity::getId, Function.identity()));

        // Batch query cho Systems
        List<ReasonMapEntity> allMappings = reasonMapRepository.findByReasonIdIn(reasonIds);
        Set<Long> allSystemIds = allMappings.stream()
                .map(ReasonMapEntity::getSystemId)
                .collect(Collectors.toSet());

        Map<Long, SystemDTO> systemDTOMap = allSystemIds.isEmpty()
                ? Collections.emptyMap()
                : systemProxy.getSystems(allSystemIds);

        // Set data cho DTOs
        for (ReasonDTO dto : dtoList) {

            Optional.ofNullable(classifyReasonMap.get(dto.getClassifyReason().getId()))
                    .ifPresent(classify -> reasonMapper.setClassifyReason(dto, classify));

            // Set Systems
            dto.setSystems(getSystemsByReasonIdOptimized(dto.getId(), allMappings, systemDTOMap));
        }

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }
    // ---------------- HELPER METHODS ----------------
    private void saveReasonSystemMap(Long reasonId, Set<Long> systemIds) {
        List<ReasonMapEntity> mapEntities = systemIds.stream().map(systemId -> {
            ReasonMapEntity mapEntity = new ReasonMapEntity();
            mapEntity.setReasonId(reasonId);
            mapEntity.setSystemId(systemId);
            return mapEntity;
        }).collect(Collectors.toList());

        reasonMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<SystemDTO> getSystemsByReasonId(Long reasonId) {
        List<ReasonMapEntity> mapEntities = reasonMapRepository.findByReasonId(reasonId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> systemIds = mapEntities.stream().map(ReasonMapEntity::getSystemId).collect(Collectors.toSet());
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(systemIds);
        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<SystemDTO> getSystemsByReasonIdOptimized(Long reasonId, List<ReasonMapEntity> allMappings, Map<Long, SystemDTO> systemDTOMap) {
        Set<Long> systemIds = allMappings.stream().filter(mapping -> mapping.getReasonId().equals(reasonId)).map(ReasonMapEntity::getSystemId).collect(Collectors.toSet());

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = reasonRepository.existsByName(name);
        } else {
            exists = reasonRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = reasonRepository.existsByCode(code);
        } else {
            exists = reasonRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!reasonRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }

}
