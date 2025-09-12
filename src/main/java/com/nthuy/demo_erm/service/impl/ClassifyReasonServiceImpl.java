package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
import com.nthuy.demo_erm.proxy.SystemProxy;
import com.nthuy.demo_erm.repository.ClassifyReasonMapRepository;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.service.ClassifyReasonService;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassifyReasonServiceImpl implements ClassifyReasonService {

    private final ClassifyReasonRepository classifyReasonRepository;
    private final ClassifyReasonMapper classifyReasonMapper;
    private final ClassifyReasonMapRepository classifyReasonMapRepository;
    private final SystemProxy systemProxy;



    // ---------------- CREATE ----------------
    @Override
    @Transactional
    public Long create(ClassifyReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), null);
        this.validateCodeNotExists(dto.getCode(), null);
        ClassifyReasonEntity entity = classifyReasonMapper.toEntity(dto);
        classifyReasonRepository.save(entity);

        // xử lý systems
        Set<Long> systemIds = (dto.getSystem() != null && !dto.getSystem().isEmpty()) ? dto.getSystem().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));

        saveClassifyReasonSystemMap(entity.getId(), systemIds);

        return entity.getId();
    }

    // ---------------- GET BY ID ----------------
    @Override
    public ClassifyReasonDTO getClassifyReason(Long id) {
        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));

        ClassifyReasonDTO dto = classifyReasonMapper.toDto(classifyReason);

        // Tối ưu: Chỉ lấy systems cho 1 reason này
        dto.setSystem(getSystemsByClassifyReasonId(id));

        return dto;
    }

    // ---------------- DELETE ----------------
    @Override
    public void delete(Long id) {
        this.validateIdExists(id);
        classifyReasonRepository.deleteById(id);
    }

    // ---------------- UPDATE ----------------
    @Override
    @Transactional
    public Long update(ClassifyReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), dto.getId());
        this.validateCodeNotExists(dto.getCode(), dto.getId());

        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));

        classifyReasonMapper.updateEntityFromDto(dto, classifyReason);
        classifyReasonRepository.save(classifyReason);

        Set<Long> systemIds = (dto.getSystem() != null && !dto.getSystem().isEmpty()) ? dto.getSystem().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));

        classifyReasonMapRepository.deleteByClassifyReasonId(classifyReason.getId());
        saveClassifyReasonSystemMap(classifyReason.getId(), systemIds);

        return classifyReason.getId();
    }

    // ---------------- GET LIST - ĐÃ TỐI ƯU ----------------
    @Override
    @Transactional(readOnly = true)
    public ResultPaginationDTO<ClassifyReasonDTO> getListClassifyReason(String code, String name, List<Long> systemIds, Pageable pageable) {

        Specification<ClassifyReasonEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, name, ClassifyReasonSpecification::hasName);
        spec = SpecificationUtils.addIfNotEmpty(spec, systemIds, ClassifyReasonSpecification::hasSystemIdIn);

        Page<ClassifyReasonEntity> pageResult = classifyReasonRepository.findAll(spec, pageable);

        List<ClassifyReasonDTO> dtoList = classifyReasonMapper.toDtoList(pageResult.getContent());

        //Thu thập tất cả systemIds cần thiết từ tất cả reasxons
        Set<Long> allSystemIds = new HashSet<>();
        List<Long> reasonIds = dtoList.stream().map(ClassifyReasonDTO::getId).collect(Collectors.toList());

        //Lấy tất cả mapping của các reasons này
        List<ClassifyReasonMapEntity> allMappings = classifyReasonMapRepository.findByClassifyReasonIdIn(reasonIds);
        allMappings.forEach(mapping -> allSystemIds.add(mapping.getSystemId()));

        //call FeignClient để lấy tất cả systems cần thiết
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(allSystemIds);
        // Gán systems cho từng reason
        for (ClassifyReasonDTO dto : dtoList) {
            dto.setSystem(getSystemsByClassifyReasonIdOptimized(dto.getId(), allMappings, systemDTOMap));
        }

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ---------------- HELPER METHODS ----------------
    private void saveClassifyReasonSystemMap(Long reasonId, Set<Long> systemIds) {
        List<ClassifyReasonMapEntity> mapEntities = systemIds.stream().map(systemId -> {
            ClassifyReasonMapEntity mapEntity = new ClassifyReasonMapEntity();
            mapEntity.setClassifyReasonId(reasonId);
            mapEntity.setSystemId(systemId);
            return mapEntity;
        }).collect(Collectors.toList());

        classifyReasonMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<SystemDTO> getSystemsByClassifyReasonId(Long classifyReasonId) {
        List<ClassifyReasonMapEntity> mapEntities = classifyReasonMapRepository.findByClassifyReasonId(classifyReasonId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> systemIds = mapEntities.stream().map(ClassifyReasonMapEntity::getSystemId).collect(Collectors.toSet());
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(systemIds);
        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<SystemDTO> getSystemsByClassifyReasonIdOptimized(Long classifyReasonId, List<ClassifyReasonMapEntity> allMappings, Map<Long, SystemDTO> systemDTOMap) {
        Set<Long> systemIds = allMappings.stream().filter(mapping -> mapping.getClassifyReasonId().equals(classifyReasonId)).map(ClassifyReasonMapEntity::getSystemId).collect(Collectors.toSet());

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = classifyReasonRepository.existsByName(name);
        } else {
            exists = classifyReasonRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = classifyReasonRepository.existsByCode(code);
        } else {
            exists = classifyReasonRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!classifyReasonRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }

}