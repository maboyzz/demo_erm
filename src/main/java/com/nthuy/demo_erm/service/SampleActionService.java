package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SampleActionDTO;


import org.springframework.data.domain.Pageable;



public interface SampleActionService {
    Long create(SampleActionDTO sampleDto) throws NameExisted;

    SampleActionDTO getSampleAction(Long id);

    void delete(Long id);

    Long update(SampleActionDTO dto) throws NameExisted;

    ResultPaginationDTO<SampleActionDTO> getListSampleAction(String code, String name, Long riskTypeId, Long classifyReasonId, Boolean isActive, Pageable pageable);
}
