package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface AttributeService {
    boolean nameExists(String userName);


    boolean existsById(Long id);


    Long handleCreateAttribute(AttributeDTO dto);


    AttributeDTO handleGetAttributeById(Long id);


    void handleDeleteAttribute(Long id);


    Long handleUpdateAttribute(AttributeDTO dto);

    ResultPaginationDTO<AttributeDTO> handleGetAttribute(String code, String name, Boolean isActive, Pageable pageable);

    void validateAttribute(AttributeDTO dto);
}
