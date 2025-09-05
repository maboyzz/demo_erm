package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.RiskCategorySpecification;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.entity.RiskCategoryMapEntity;
import com.nthuy.demo_erm.mapper.RiskCategoryMapper;
import com.nthuy.demo_erm.proxy.SystemProxy;
import com.nthuy.demo_erm.repository.RiskCategoryMapRepository;
import com.nthuy.demo_erm.repository.RiskCategoryRepository;
import com.nthuy.demo_erm.service.RiskCategoryService;
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
public class RiskCategoryServiceImpl implements RiskCategoryService {

    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapper riskCategoryMapper;
    private final RiskCategoryMapRepository riskCategoryMapRepository;
    private final SystemProxy systemProxy;


    @Override
    @Transactional
    public Long create(RiskCategoryDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), null);
        this.validateCodeNotExists(dto.getCode(), null);
        RiskCategoryEntity entity = riskCategoryMapper.toEntity(dto);
        riskCategoryRepository.save(entity);

        // xử lý systems
        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));
        saveRiskCategorySystemMap(entity.getId(), systemIds);

        return entity.getId();
    }

    @Override
    public RiskCategoryDTO getRiskCategory(Long id) {

        RiskCategoryEntity riskCategory = riskCategoryRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));

        RiskCategoryDTO dto = riskCategoryMapper.toDto(riskCategory);

        // Tối ưu: Chỉ lấy systems cho 1 reason này
        dto.setSystems(getSystemsByRiskCategoryId(id));

        return dto;
    }

    @Override
    public void gelete(Long id) {
        this.validateIdExists(id);
        this.riskCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(RiskCategoryDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), dto.getId());
        this.validateCodeNotExists(dto.getCode(), dto.getId());
        Long parentId = dto.getParent_id();
        if (parentId != null) {
            // 1) parent không được trùng với chính nó
            if (parentId.equals(dto.getId())) {
                throw new IdInvalidException("ID danh mục cha không được trùng với chính nó: " + parentId);
            }
            // 2) parent phải tồn tại
            this.validateIdExists(parentId);

        }
        RiskCategoryEntity entity = riskCategoryRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Danh mục rủi ro với ID " + dto.getId() + " không tồn tại"));

        riskCategoryMapper.updateEntityFromDto(dto, entity);
        riskCategoryRepository.save(entity);

        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));

        riskCategoryMapRepository.deleteByRiskCategoryId(entity.getId());
        saveRiskCategorySystemMap(entity.getId(),systemIds);

        return entity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPaginationDTO<RiskCategoryDTO> getListRiskCategory(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable) {

        Specification<RiskCategoryEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfNotBlank(spec, code, RiskCategorySpecification::hasCode);
        spec = SpecificationUtils.addIfNotBlank(spec, name, RiskCategorySpecification::hasName);
        spec = SpecificationUtils.addIfNotEmpty(spec, systemIds, RiskCategorySpecification::hasSystemIdIn);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, RiskCategorySpecification::hasIsActive);

        Page<RiskCategoryEntity> pageResult = riskCategoryRepository.findAll(spec, pageable);

        List<RiskCategoryDTO> dtoList = riskCategoryMapper.toDtoList(pageResult.getContent());

        //Thu thập tất cả systemIds cần thiết từ tất cả reasxons
        Set<Long> allSystemIds = new HashSet<>();
        List<Long> reasonIds = dtoList.stream().map(RiskCategoryDTO::getId).collect(Collectors.toList());

        //Lấy tất cả mapping của các reasons này
        List<RiskCategoryMapEntity> allMappings = riskCategoryMapRepository.findByRiskCategoryIdIn(reasonIds);
        allMappings.forEach(mapping -> allSystemIds.add(mapping.getSystemId()));

        //call FeignClient để lấy tất cả systems cần thiết
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(allSystemIds);
        // Gán systems cho từng reason
        for (RiskCategoryDTO dto : dtoList) {
            dto.setSystems(getSystemsByRiskCategoryIdOptimized(dto.getId(), allMappings, systemDTOMap));
        }

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ---------------- HELPER METHODS ----------------
    private void saveRiskCategorySystemMap(Long riskCategoryId, Set<Long> systemIds) {
        List<RiskCategoryMapEntity> mapEntities = systemIds.stream().map(systemId -> {
            RiskCategoryMapEntity mapEntity = new RiskCategoryMapEntity();
            mapEntity.setRiskCategoryId(riskCategoryId);
            mapEntity.setSystemId(systemId);
            return mapEntity;
        }).collect(Collectors.toList());

        riskCategoryMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<SystemDTO> getSystemsByRiskCategoryId(Long riskCategoryId) {
        List<RiskCategoryMapEntity> mapEntities = riskCategoryMapRepository.findByRiskCategoryId(riskCategoryId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> systemIds = mapEntities.stream().map(RiskCategoryMapEntity::getSystemId).collect(Collectors.toSet());
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(systemIds);
        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<SystemDTO> getSystemsByRiskCategoryIdOptimized(Long riskCategoryId, List<RiskCategoryMapEntity> allMappings, Map<Long, SystemDTO> systemDTOMap) {
        Set<Long> systemIds = allMappings.stream().filter(mapping -> mapping.getRiskCategoryId().equals(riskCategoryId)).map(RiskCategoryMapEntity::getSystemId).collect(Collectors.toSet());

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskCategoryRepository.existsByName(name);
        } else {
            exists = riskCategoryRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskCategoryRepository.existsByCode(code);
        } else {
            exists = riskCategoryRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!riskCategoryRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }
}
