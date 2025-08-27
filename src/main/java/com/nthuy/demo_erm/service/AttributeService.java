package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface AttributeService {
    Long handleCreateAttribute(AttributeDTO dto) throws NameExisted;

    AttributeDTO handleGetAttributeById(Long id);

    void handleDeleteAttribute(Long id);

    Long handleUpdateAttribute(AttributeDTO dto);

    ResultPaginationDTO<AttributeDTO> handleGetAttribute(String code, String name, Boolean isActive, Pageable pageable);

}
