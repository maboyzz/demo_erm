package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassifyReasonService {

    Long create(ClassifyReasonDTO dto) throws NameExisted;

    ClassifyReasonDTO getClassifyReason(Long id);

    void delete(Long id);

    Long update(ClassifyReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<ClassifyReasonDTO> getListClassifyReason(String code, String name, List<Long> systemIds, Pageable pageable);
}
