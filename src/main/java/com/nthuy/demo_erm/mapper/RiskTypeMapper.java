package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.dto.response.AttributeGroupResponse;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import com.nthuy.demo_erm.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RiskTypeMapper {

    // RiskType
    RiskTypeDTO toRiskTypeDto(RiskTypeEntity entity);
    RiskTypeEntity toRiskTypeEntity(RiskTypeDTO dto);


    // AttributeRiskType
    @Mapping(source = "attributeGroup.id", target = "attributeGroupId")
    @Mapping(source = "attribute.id", target = "attributeId")
    AttributeRiskTypeEntity toRiskTypeAttributeEntity(RiskTypeAttributeDTO dto);

    @Mapping(source = "attributeGroupId", target = "attributeGroup.id")
    @Mapping(source = "attributeId", target = "attribute.id")
    RiskTypeAttributeDTO toRiskTypeAttributeDto(AttributeRiskTypeEntity entity);

    // AttributeRiskTypeValue
    @Mapping(source = "attributeValueDTO.id", target = "attributeValueId")
    AttributeRiskTypeValueEntity toRiskTypeAttributeValueEntity(RiskTypeAttributeValueDTO dto);

    @Mapping(source = "attributeValueId", target = "attributeValueDTO.id")
    RiskTypeAttributeValueDTO toRiskTypeAttributeValueDto(AttributeRiskTypeValueEntity entity);

    // Collections
    List<RiskTypeAttributeDTO> toRiskTypeAttributeDtos(List<AttributeRiskTypeEntity> entities);
    List<RiskTypeAttributeValueDTO> toRiskTypeAttributeValueDtos(List<AttributeRiskTypeValueEntity> entities);

    void updateRiskTypeEntityFromDto(RiskTypeDTO dto, @MappingTarget RiskTypeEntity entity);

    //res
    RiskTypeRes toDtoRes(RiskTypeEntity entity);

    List<RiskTypeRes> toDtoListRes(List<RiskTypeEntity> entities);

    IdCodeNameResponse toAttributeGroupDto(AttributeGroupEntity group);
}