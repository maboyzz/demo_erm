package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.dto.Meta;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.AttributeGroupSpecification;
import com.nthuy.demo_erm.common.constant.EnumTypeAttributeGroup;
import com.nthuy.demo_erm.config.ReasonSpecification;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.exception.TypeAttributeGroupValidException;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.mapper.AttributeGroupMapper;
import com.nthuy.demo_erm.repository.AttributeGroupRepository;
import com.nthuy.demo_erm.service.AttributeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AttributeGroupServiceImpl implements AttributeGroupService {

    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeGroupMapper attributeGroupMapper;


    @Override
    public Long create(AttributeGroupDTO dto) throws NameExisted {
        validateCodeNotExists(dto.getCode(), null);
        validateNameNotExists(dto.getName(), null);

        AttributeGroupEntity entity = attributeGroupMapper.toEntity(dto);
        if (entity.getType() != null) {
            AttributeGroupEntity savedEntity = attributeGroupRepository.save(entity);
            return savedEntity.getId();
        } else {
            entity.setType(EnumTypeAttributeGroup.BUSINESS);
            AttributeGroupEntity savedEntity = attributeGroupRepository.save(entity);
            return savedEntity.getId();
        }
    }


    @Override
    public AttributeGroupDTO getAttributeGroup(Long id) {
        AttributeGroupEntity entity = this.attributeGroupRepository.findById(id).orElseThrow(() -> new BadRequestValidationException(id + " không tồn tại"));
        return attributeGroupMapper.toDto(entity);

    }

    @Override
    public void delete(Long id) {
        AttributeGroupEntity entity = attributeGroupRepository.findById(id)
                .orElseThrow(() -> new BadRequestValidationException("Id không tồn tại: " + id));

        if (EnumTypeAttributeGroup.SYSTEM.equals(entity.getType())) {
            throw new TypeAttributeGroupValidException("SYSTEM không thể xóa");
        }

        attributeGroupRepository.delete(entity);

    }

    @Override
    public Long update(AttributeGroupDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), dto.getId());
        this.validateCodeNotExists(dto.getCode(), dto.getId());

        AttributeGroupEntity entity = attributeGroupRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Nhóm thuộc tính với ID " + dto.getId() + " không tồn tại"));
        if (EnumTypeAttributeGroup.SYSTEM.equals(entity.getType())) {
            throw new TypeAttributeGroupValidException("SYSTEM không thể chỉnh sửa");
        }
        attributeGroupMapper.updateEntityFromDto(dto, entity);
        return this.attributeGroupRepository.save(entity).getId();
    }

    @Override
    public ResultPaginationDTO<AttributeGroupDTO> getListAttributeGroup(String code, String name, Boolean isActive, Pageable pageable) {
        Specification<AttributeGroupEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfNotBlank(spec, code, AttributeGroupSpecification::hasCode);
        spec = SpecificationUtils.addIfNotBlank(spec, name, AttributeGroupSpecification::hasName);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, AttributeGroupSpecification::hasIsActive);


        Page<AttributeGroupEntity> pageResult = attributeGroupRepository.findAll(spec, pageable);
        List<AttributeGroupEntity> attributeGroupEntities = pageResult.getContent();

        if (attributeGroupEntities.isEmpty()) {
            return PaginationUtils.buildResult(pageResult, Collections.emptyList(), pageable);
        }

        List<AttributeGroupDTO> dtoList = attributeGroupMapper.toDtoList(attributeGroupEntities);

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ---------------- HELPER METHODS ----------------

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = attributeGroupRepository.existsByName(name);
        } else {
            exists = attributeGroupRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = attributeGroupRepository.existsByCode(code);
        } else {
            exists = attributeGroupRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!attributeGroupRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }

}
