package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import org.springframework.data.domain.Pageable;

public interface ClassifyReasonService {

    Long create(ClassifyReasonDTO dto) throws NameExisted;

    ClassifyReasonDTO getClassifyReason(Long id);

    void delete(Long id);

    Long update(ClassifyReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<ClassifyReasonDTO> getListClassifyReason(SearchRequest searchRequest, Pageable pageable);
}
