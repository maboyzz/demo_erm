package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;

import java.util.List;

public interface ClassifyReasonService {

    boolean nameExists(String userName);


    boolean existsById(Long id);


    Long handleCreateClassifyReason(ClassifyReasonDTO dto);


    ClassifyReasonDTO handleGetClassifyReasonById(Long id);


    void handleDeleteClassifyReason(Long id);


    List<ClassifyReasonDTO> handleGetClassifyReason(String code, String name, List<Long> systemIds);
}
