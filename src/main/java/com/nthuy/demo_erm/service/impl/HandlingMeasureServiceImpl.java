package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.HandlingMeasureDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.service.HandlingMeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandlingMeasureServiceImpl implements HandlingMeasureService {
    @Override
    public Long create(HandlingMeasureDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public HandlingMeasureDTO getHandlingMeasure(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Long update(HandlingMeasureDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<HandlingMeasureDTO> getListHandlingMeasure(String code, String name, Pageable pageable) {
        return null;
    }
}
