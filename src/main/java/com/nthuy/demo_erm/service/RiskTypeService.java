package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.dto.RiskTypeDTO;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskTypeService {
    Long create(RiskTypeDTO dto) throws NameExisted;

    RiskTypeDTO getRiskType(Long id);

    void delete(Long id);

    Long update(RiskTypeDTO dto) throws NameExisted;

    ResultPaginationDTO<RiskTypeRes> getListRiskType(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable);
}
