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

    @Mapping(source = "riskTypeId", target = "riskType.id")
    @Mapping(source = "riskCategoryId", target = "riskCategory.id")
    @Mapping(source = "reporterId", target = "reporter.id")
    @Mapping(source = "systemId", target = "system.id")
    RiskDTO toDto(RiskEntity entity);

    @Mapping(source = "riskType.id", target = "riskTypeId")
    @Mapping(source = "riskCategory.id", target = "riskCategoryId")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "system.id", target = "systemId")
    RiskEntity toEntity(RiskDTO dto);


}