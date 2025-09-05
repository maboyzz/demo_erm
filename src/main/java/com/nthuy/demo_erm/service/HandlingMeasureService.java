package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.exception.NameExisted;

import com.nthuy.demo_erm.dto.HandlingMeasureDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HandlingMeasureService {
    Long create(HandlingMeasureDTO dto) throws NameExisted;

    HandlingMeasureDTO getHandlingMeasure(Long id);

    void delete(Long id);

    Long update(HandlingMeasureDTO dto) throws NameExisted;

    ResultPaginationDTO<HandlingMeasureDTO> getListHandlingMeasure(String code, String name,Boolean isActive, Pageable pageable);
}
