package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.config.AttributeGroupSpecification;
import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.exception.TypeAttributeGroupValidException;
import com.nthuy.demo_erm.mapper.AttributeGroupMapper;
import com.nthuy.demo_erm.repository.AttributeGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttributeGroupServiceImpl implements AttributeGroupService{

   private final AttributeGroupRepository attributeGroupRepository;
   private final AttributeGroupMapper attributeGroupMapper;

    public AttributeGroupServiceImpl(AttributeGroupRepository attributeGroupRepository, AttributeGroupMapper attributeGroupMapper) {
        this.attributeGroupRepository = attributeGroupRepository;
        this.attributeGroupMapper = attributeGroupMapper;
    }


    @Override
    public boolean nameExists(String userName) {
        return this.attributeGroupRepository.existsByName(userName);
    }

    @Override
    public boolean existsById(Long id) {
        return this.attributeGroupRepository.existsById(id);
    }

    @Override
    public Long handleCreateAttributeGroup(AttributeGroupDTO dto) {
        AttributeGroupEntity entity = attributeGroupMapper.toEntity(dto);
        if (entity.getType()!=null){
            System.out.println("Entity trước khi lưu: " + entity);
            AttributeGroupEntity savedEntity = attributeGroupRepository.save(entity);
            return savedEntity.getId();
        }else {
            entity.setType(EnumTypeAttributeGroup.BUSINESS);
            System.out.println("Entity trước khi lưu: " + entity);
            AttributeGroupEntity savedEntity = attributeGroupRepository.save(entity);
            return savedEntity.getId();
        }
    }


    @Override
    public AttributeGroupDTO handleGetAttributeGroupById(Long id) {
        AttributeGroupEntity entity = this.attributeGroupRepository.findById(id).orElseThrow(() -> new BadRequestValidationException(id + " không tồn tại"));
        return attributeGroupMapper.toDto(entity);

    }

    @Override
    public void handleDeleteAttributeGroup(Long id) {
        AttributeGroupDTO dto = this.handleGetAttributeGroupById(id);
        if (!dto.getType().equals(EnumTypeAttributeGroup.SYSTEM)){
            this.attributeGroupRepository.deleteById(dto.getId());
        }else throw new  TypeAttributeGroupValidException("SYSTEM không thể xóa");

    }

    @Override
    public Long handleUpdateAttributeGroup(AttributeGroupDTO dto) {
        AttributeGroupEntity entity = attributeGroupRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestValidationException(
                        "Nhóm thuộc tính với ID " + dto.getId() + " không tồn tại"));
        attributeGroupMapper.updateEntityFromDto(dto,entity);
        if (entity.getType().equals(EnumTypeAttributeGroup.SYSTEM)){
            throw new TypeAttributeGroupValidException("System không thể chỉnh sửa");
        }
        return this.attributeGroupRepository.save(entity).getId();
    }

    @Override
    public ResultPaginationDTO<AttributeGroupDTO> handleGetAttributeGroup(String code, String name, Boolean isActive, Pageable pageable) {
        Specification<AttributeGroupEntity> spec = Specification
                .where(AttributeGroupSpecification.hasCode(code))
                .and(AttributeGroupSpecification.hasName(name))
                .and(AttributeGroupSpecification.hasIsActive(isActive));

        Page<AttributeGroupEntity> pageResult = attributeGroupRepository.findAll(spec, pageable);

        List<AttributeGroupDTO> dtoList = attributeGroupMapper.toDtoList(pageResult.getContent());

        Meta meta = new Meta();
        meta.setPage(pageResult.getNumber());
        meta.setSize(pageResult.getSize());
        meta.setTotalElements(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setNumberOfElements(pageResult.getNumberOfElements());
        meta.setSort(pageable.getSort().toString());

        ResultPaginationDTO<AttributeGroupDTO> result = new ResultPaginationDTO<>();
        result.setContent(dtoList);
        result.setMeta(meta);

        return result;
    }
}
