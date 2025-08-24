package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassifyReasonService {

    boolean nameExists(String userName);


    boolean existsById(Long id);


    Long handleCreateClassifyReason(ClassifyReasonDTO dto);


    ClassifyReasonDTO handleGetClassifyReasonById(Long id);


    void handleDeleteClassifyReason(Long id);


    Long handleUpdateClassifyReason(ClassifyReasonDTO dto);

    ResultPaginationDTO<ClassifyReasonDTO> handleGetClassifyReason(String code, String name, List<Long> systemIds, Pageable pageable);
}
