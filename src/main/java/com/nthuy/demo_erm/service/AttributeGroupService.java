package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeGroupDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttributeGroupService {
    boolean nameExists(String userName);


    boolean existsById(Long id);


    Long handleCreateAttributeGroup(AttributeGroupDTO dto);


    AttributeGroupDTO handleGetAttributeGroupById(Long id);


    void handleDeleteAttributeGroup(Long id);


    Long handleUpdateAttributeGroup(AttributeGroupDTO dto);

    ResultPaginationDTO<AttributeGroupDTO> handleGetAttributeGroup(String code, String name, List<Long> systemIds, Pageable pageable);
}
