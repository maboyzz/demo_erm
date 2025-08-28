package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeGroupDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import org.springframework.data.domain.Pageable;

public interface AttributeGroupService {

    Long handleCreateAttributeGroup(AttributeGroupDTO dto) throws NameExisted;

    AttributeGroupDTO handleGetAttributeGroupById(Long id);

    void handleDeleteAttributeGroup(Long id);

    Long handleUpdateAttributeGroup(AttributeGroupDTO dto) throws NameExisted;

    ResultPaginationDTO<AttributeGroupDTO> handleGetAttributeGroup(String code, String name, Boolean isActive, Pageable pageable);
}
