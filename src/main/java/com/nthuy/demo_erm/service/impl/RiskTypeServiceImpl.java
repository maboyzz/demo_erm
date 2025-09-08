package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.RiskTypeSpecification;
import com.nthuy.demo_erm.dto.*;

import com.nthuy.demo_erm.dto.response.AttributeResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import com.nthuy.demo_erm.entity.*;
import com.nthuy.demo_erm.mapper.AttributeMapper;
import com.nthuy.demo_erm.mapper.AttributeValueMapper;
import com.nthuy.demo_erm.mapper.RiskTypeMapper;
import com.nthuy.demo_erm.proxy.SystemProxy;
import com.nthuy.demo_erm.repository.*;
import com.nthuy.demo_erm.service.RiskTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskTypeServiceImpl implements RiskTypeService {

    private final RiskTypeRepository riskTypeRepository;
    private final RiskTypeAttributeRepository riskTypeAttributeRepository;
    private final RiskTypeAttributeValueRepository riskTypeAttributeValueRepository;
    private final SystemProxy systemProxy;
    private final RiskTypeMapper riskTypeMapper;
    private final RiskTypeMapRepository riskTypeMapRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;


    @Override
    @Transactional
    public Long create(RiskTypeDTO dto) throws NameExisted {
        validateNameNotExists(dto.getName(), null);
        validateCodeNotExists(dto.getCode(), null);

        RiskTypeEntity riskTypeEntity = riskTypeMapper.toRiskTypeEntity(dto);
        riskTypeEntity = riskTypeRepository.save(riskTypeEntity);

        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));
        saveRiskTypeSystemMap(riskTypeEntity.getId(), systemIds);

        if (dto.getGroups() != null) {
            for (RiskTypeAttributeDTO attrDto : dto.getGroups()) {
                AttributeRiskTypeEntity attrEntity = riskTypeMapper.toRiskTypeAttributeEntity(attrDto);
                attrEntity.setRiskTypeId(riskTypeEntity.getId()); // gắn FK cha
                attrEntity = riskTypeAttributeRepository.save(attrEntity);

                // --- Lưu values ---
                if (attrDto.getRiskTypeAttributeValue() != null) {
                    for (RiskTypeAttributeValueDTO valueDto : attrDto.getRiskTypeAttributeValue()) {
                        AttributeRiskTypeValueEntity valueEntity = riskTypeMapper.toRiskTypeAttributeValueEntity(valueDto);

                        // FK đúng
                        valueEntity.setRiskTypeAttributeId(attrEntity.getId());
                        valueEntity.setAttributeValueId(valueDto.getAttributeValueDTO().getId());

                        riskTypeAttributeValueRepository.save(valueEntity);

                    }
                }
            }
        }

        return riskTypeEntity.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public RiskTypeDTO getRiskType(Long id) {
        // 1. Lấy riskType
        RiskTypeEntity riskType = riskTypeRepository.findById(id).orElseThrow(() -> new IdInvalidException("Không tìm thấy riskType id=" + id));

        RiskTypeDTO dto = riskTypeMapper.toRiskTypeDto(riskType);

        // 2. Lấy AttributeRiskType theo riskTypeId
        List<AttributeRiskTypeEntity> attrEntities = riskTypeAttributeRepository.findByRiskTypeId(id);

        List<RiskTypeAttributeDTO> groupDtos = new ArrayList<>();
        for (AttributeRiskTypeEntity attrEntity : attrEntities) {
            RiskTypeAttributeDTO groupDto = riskTypeMapper.toRiskTypeAttributeDto(attrEntity);

            // ---- load attributeGroup đầy đủ ----
            if (attrEntity.getAttributeGroupId() != null) {
                attributeGroupRepository.findById(attrEntity.getAttributeGroupId()).ifPresent(group -> groupDto.setAttributeGroup(riskTypeMapper.toAttributeGroupDto(group)));
            }

            // ---- load attribute đầy đủ ----
            if (attrEntity.getAttributeId() != null) {
                attributeRepository.findById(attrEntity.getAttributeId()).ifPresent(attribute -> {
                    AttributeResponse attributeDTO = attributeMapper.toDtoRes(attribute);

                    // ---- load values cho attribute ----
                    List<AttributeValueEntity> valueEntities = attributeValueRepository.findByAttributeId(attribute.getId());
                    attributeDTO.setValues(valueEntities.stream().map(attributeValueMapper::toDto)  // ✅ đúng mapper
                            .collect(Collectors.toList()));
                    ;

                    groupDto.setAttribute(attributeDTO);
                });
            }

            // ---- load riskTypeAttributeValue đầy đủ ----
            List<AttributeRiskTypeValueEntity> valueEntities = riskTypeAttributeValueRepository.findByRiskTypeAttributeId(attrEntity.getId());

            List<RiskTypeAttributeValueDTO> valueDtos = new ArrayList<>();
            for (AttributeRiskTypeValueEntity v : valueEntities) {
                RiskTypeAttributeValueDTO vDto = riskTypeMapper.toRiskTypeAttributeValueDto(v);

                if (v.getAttributeValueId() != null) {
                    attributeValueRepository.findById(v.getAttributeValueId()).ifPresent(av -> vDto.setAttributeValueDTO(attributeValueMapper.toDto(av)));
                }
                valueDtos.add(vDto);
            }
            groupDto.setRiskTypeAttributeValue(valueDtos);

            groupDtos.add(groupDto);
        }
        dto.setGroups(groupDtos);

        dto.setSystems(getSystemsByRiskTypeId(id));


        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateIdExists(id);
        riskTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(RiskTypeDTO dto) throws NameExisted {
        validateCodeNotExists(dto.getCode(), dto.getId());
        validateNameNotExists(dto.getName(), dto.getId());

        // 1. Lấy entity hiện tại
        RiskTypeEntity existing = riskTypeRepository.findById(dto.getId()).orElseThrow(() -> new IdInvalidException("Không tìm thấy riskType id=" + dto.getId()));

        // 2. Map lại field từ DTO vào entity (dùng mapper hoặc thủ công)
        riskTypeMapper.updateRiskTypeEntityFromDto(dto, existing);

        // 3. Lưu entity
        RiskTypeEntity saved = riskTypeRepository.save(existing);

        // 4. Update systems (xoá cũ -> thêm mới)
        riskTypeMapRepository.deleteByRiskTypeId(saved.getId());
        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));
        saveRiskTypeSystemMap(saved.getId(), systemIds);

        // 5. Update attribute groups
        riskTypeAttributeRepository.deleteByRiskTypeId(saved.getId());
        if (dto.getGroups() != null) {
            for (RiskTypeAttributeDTO attrDto : dto.getGroups()) {
                AttributeRiskTypeEntity attrEntity = riskTypeMapper.toRiskTypeAttributeEntity(attrDto);
                attrEntity.setRiskTypeId(saved.getId());
                attrEntity = riskTypeAttributeRepository.save(attrEntity);

                // Lưu values
                if (attrDto.getRiskTypeAttributeValue() != null) {
                    for (RiskTypeAttributeValueDTO valueDto : attrDto.getRiskTypeAttributeValue()) {
                        AttributeRiskTypeValueEntity valueEntity = riskTypeMapper.toRiskTypeAttributeValueEntity(valueDto);
                        valueEntity.setRiskTypeAttributeId(attrEntity.getId());
                        valueEntity.setAttributeValueId(valueDto.getAttributeValueDTO().getId());
                        riskTypeAttributeValueRepository.save(valueEntity);
                    }
                }
            }
        }

        return saved.getId();
    }

    @Override
    public ResultPaginationDTO<RiskTypeRes> getListRiskType(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable) {
        Specification<RiskTypeEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, code, RiskTypeSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, name, RiskTypeSpecification::hasName);
        spec = SpecificationUtils.addIfNotEmpty(spec, systemIds, RiskTypeSpecification::hasSystemIdIn);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, RiskTypeSpecification::hasIsActive);

        Page<RiskTypeEntity> pageResult = riskTypeRepository.findAll(spec, pageable);

        List<RiskTypeRes> dtoListRes = riskTypeMapper.toDtoListRes(pageResult.getContent());

        //Thu thập tất cả systemIds cần thiết từ tất cả reasxons
        Set<Long> allSystemIds = new HashSet<>();
        List<Long> riskTypeIds = dtoListRes.stream().map(RiskTypeRes::getId).collect(Collectors.toList());

        //Lấy tất cả mapping của các reasons này
        List<RiskTypeMapEntity> allMappings = riskTypeMapRepository.findByRiskTypeIdIn(riskTypeIds);
        allMappings.forEach(mapping -> allSystemIds.add(mapping.getSystemId()));

        //call FeignClient để lấy tất cả systems cần thiết
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(allSystemIds);
        // Gán systems cho từng reason
        for (RiskTypeRes dto : dtoListRes) {
            dto.setSystems(getSystemsByRiskTypeIdOptimized(dto.getId(), allMappings, systemDTOMap));
        }

        return PaginationUtils.buildResult(pageResult, dtoListRes, pageable);
    }

    // ---------------- HELPER METHODS ----------------
    private void saveRiskTypeSystemMap(Long riskTypeID, Set<Long> systemIds) {
        List<RiskTypeMapEntity> mapEntities = systemIds.stream().map(systemId -> {
            RiskTypeMapEntity mapEntity = new RiskTypeMapEntity();
            mapEntity.setRiskTypeId(riskTypeID);
            mapEntity.setSystemId(systemId);
            return mapEntity;
        }).collect(Collectors.toList());

        riskTypeMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<SystemDTO> getSystemsByRiskTypeId(Long riskTypeId) {
        List<RiskTypeMapEntity> mapEntities = riskTypeMapRepository.findByRiskTypeId(riskTypeId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> systemIds = mapEntities.stream().map(RiskTypeMapEntity::getSystemId).collect(Collectors.toSet());
        Map<Long, SystemDTO> systemDTOMap = systemProxy.getSystems(systemIds);
        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<SystemDTO> getSystemsByRiskTypeIdOptimized(Long riskTypeId, List<RiskTypeMapEntity> allMappings, Map<Long, SystemDTO> systemDTOMap) {
        Set<Long> systemIds = allMappings.stream().filter(mapping -> mapping.getRiskTypeId().equals(riskTypeId)).map(RiskTypeMapEntity::getSystemId).collect(Collectors.toSet());

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskTypeRepository.existsByName(name);
        } else {
            exists = riskTypeRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskTypeRepository.existsByCode(code);
        } else {
            exists = riskTypeRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!riskTypeRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }
}
