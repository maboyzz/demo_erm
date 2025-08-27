package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RiskCategoryService {

    Long handleCreateRiskCategory(RiskCategoryDTO dto) throws NameExisted;

    RiskCategoryDTO handleGetRiskCategoryById(Long id);

    void handleDeleteRiskCategory(Long id);

    Long handleUpdateRiskCategory(RiskCategoryDTO dto) throws NameExisted;

    ResultPaginationDTO<RiskCategoryDTO> handleGetRiskCategory(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable);
}
