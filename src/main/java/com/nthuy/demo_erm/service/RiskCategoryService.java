package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RiskCategoryService {

    Long create(RiskCategoryDTO dto) throws NameExisted;

    RiskCategoryDTO getRiskCategory(Long id);

    void delete(Long id);

    Long update(RiskCategoryDTO dto) throws NameExisted;

    ResultPaginationDTO<RiskCategoryDTO> getListRiskCategory(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable);
}
