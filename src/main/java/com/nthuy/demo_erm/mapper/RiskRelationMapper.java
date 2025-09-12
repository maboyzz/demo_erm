package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.RiskRelationDTO;
import com.nthuy.demo_erm.entity.RiskRelationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskRelationMapper {

    @Mapping(source = "riskRelationId", target = "riskRelation.id")
    RiskRelationDTO toDto(RiskRelationEntity entity);

    @Mapping(source = "riskRelation.id", target = "riskRelationId")
    RiskRelationEntity toEntity(RiskRelationDTO dto);
}
