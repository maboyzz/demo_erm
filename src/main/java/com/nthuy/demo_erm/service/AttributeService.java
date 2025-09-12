package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import org.springframework.data.domain.Pageable;


public interface AttributeService {
    Long create(AttributeDTO dto) throws NameExisted;

    AttributeDTO getAttribute(Long id);

    void delete(Long id);

    Long update(AttributeDTO dto) throws NameExisted;

    ResultPaginationDTO<AttributeDTO> getListAttribute(SearchRequest searchRequest, Pageable pageable);

}
