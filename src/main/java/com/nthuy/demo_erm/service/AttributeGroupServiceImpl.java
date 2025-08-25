package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.mapper.AttributeGroupMapper;
import com.nthuy.demo_erm.repository.AttributeGroupRepository;
import org.springframework.data.domain.Pageable;
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
        return null;
    }

    @Override
    public void handleDeleteAttributeGroup(Long id) {

    }

    @Override
    public Long handleUpdateAttributeGroup(AttributeGroupDTO dto) {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<AttributeGroupDTO> handleGetAttributeGroup(String code, String name, List<Long> systemIds, Pageable pageable) {
        return null;
    }
}
