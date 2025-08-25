package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RiskCategoryService {

    boolean nameExists(String userName);

    boolean existsById(Long id);

    Long handleCreateRiskCategory(RiskCategoryDTO dto);

    RiskCategoryDTO handleGetRiskCategoryById(Long id);

    void handleDeleteRiskCategory(Long id);

    Long handleUpdateRiskCategory(RiskCategoryDTO dto);

    ResultPaginationDTO<RiskCategoryDTO> handleGetRiskCategory(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable);
}
