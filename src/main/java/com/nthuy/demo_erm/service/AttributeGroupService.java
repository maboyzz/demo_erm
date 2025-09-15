package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.AttributeGroupDTO;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import org.springframework.data.domain.Pageable;

public interface AttributeGroupService {

    Long create(AttributeGroupDTO dto) throws NameExisted;

    AttributeGroupDTO getAttributeGroup(Long id);

    void delete(Long id);

    Long update(AttributeGroupDTO dto) throws NameExisted;

    ResultPaginationDTO<AttributeGroupDTO> getListAttributeGroup(SearchRequest searchRequest, Pageable pageable);
}
