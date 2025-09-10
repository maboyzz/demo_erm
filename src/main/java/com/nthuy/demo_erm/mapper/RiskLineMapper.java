package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.RiskLineDTO;
import com.nthuy.demo_erm.entity.RiskLineEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskLineMapper {


    @Mapping(source = "attributeId", target = "attribute.id")
    @Mapping(source = "attributeGroupId", target = "attributeGroup.id")
    RiskLineDTO toDto(RiskLineEntity entity);

    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attributeGroup.id", target = "attributeGroupId")
    RiskLineEntity toEntity(RiskLineDTO dto);
}
