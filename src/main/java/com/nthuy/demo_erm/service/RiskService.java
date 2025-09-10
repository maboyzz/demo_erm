package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskService {
    Long create(RiskDTO dto) throws NameExisted;

    RiskDTO getRisk(Long id);

    void delete(Long id);

    Long update(RiskDTO dto) throws NameExisted;

    ResultPaginationDTO<RiskDTO> getListRisk(String code, String name, List<Long> systemIds, Boolean isActive, EnumTypeReason type, Pageable pageable);
}
