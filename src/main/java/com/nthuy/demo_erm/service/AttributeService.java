package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import org.springframework.data.domain.Pageable;


public interface AttributeService {
    Long handleCreateAttribute(AttributeDTO dto) throws NameExisted;

    AttributeDTO handleGetAttributeById(Long id);

    void handleDeleteAttribute(Long id);

    Long handleUpdateAttribute(AttributeDTO dto);

    ResultPaginationDTO<AttributeDTO> handleGetAttribute(String code, String name, Boolean isActive, Pageable pageable);

}
