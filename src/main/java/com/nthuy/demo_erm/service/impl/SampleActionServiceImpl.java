package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SampleActionDTO;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import com.nthuy.demo_erm.service.SampleActionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleActionServiceImpl implements SampleActionService {
    @Override
    public Long create(SampleActionDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public SampleActionDTO getSampleAction(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Long update(SampleActionDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<RiskTypeRes> getListSampleAction(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable) {
        return null;
    }
}
