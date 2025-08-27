package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassifyReasonService {

    Long handleCreateClassifyReason(ClassifyReasonDTO dto) throws NameExisted;

    ClassifyReasonDTO handleGetClassifyReasonById(Long id);

    void handleDeleteClassifyReason(Long id);

    Long handleUpdateClassifyReason(ClassifyReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<ClassifyReasonDTO> handleGetClassifyReason(String code, String name, List<Long> systemIds, Pageable pageable);
}
