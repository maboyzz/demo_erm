package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SampleActionDTO;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SampleActionService {
    Long create(SampleActionDTO dto) throws NameExisted;

    SampleActionDTO getSampleAction(Long id);

    void delete(Long id);

    Long update(SampleActionDTO dto) throws NameExisted;

    ResultPaginationDTO<RiskTypeRes> getListSampleAction(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable);
}
