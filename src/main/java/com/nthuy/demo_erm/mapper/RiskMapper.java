package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.EmployeeDTO;
import com.nthuy.demo_erm.dto.RiskDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.dto.response.RiskCategoryResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeResponse;
import com.nthuy.demo_erm.entity.RiskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskMapper {

    @Mapping(source = "riskTypeId", target = "riskType")
    @Mapping(source = "riskCategoryId", target = "riskCategory")
    @Mapping(source = "reporterId", target = "reporter")
    @Mapping(source = "systemId", target = "system")
    RiskDTO toDto(RiskEntity entity);

    @Mapping(source = "riskType.id", target = "riskTypeId")
    @Mapping(source = "riskCategory.id", target = "riskCategoryId")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "system.id", target = "systemId")
    RiskEntity toEntity(RiskDTO dto);

    // --- Custom mapping ---
    default RiskTypeResponse mapRiskType(Long riskTypeId) {
        if (riskTypeId == null) return null;
        return new RiskTypeResponse(riskTypeId, null, null);
    }

    default RiskCategoryResponse mapRiskCategory(Long riskCategoryId) {
        if (riskCategoryId == null) return null;
        return new RiskCategoryResponse(riskCategoryId, null, null);
    }

    default EmployeeDTO mapEmployee(Long reporterId) {
        if (reporterId == null) return null;
        return new EmployeeDTO(reporterId, null);
    }
    default SystemDTO mapSystem(Long systemId) {
        if (systemId == null) return null;
        return new SystemDTO(systemId, null);
    }
}