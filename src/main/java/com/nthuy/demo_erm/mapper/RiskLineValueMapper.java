package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.RiskLineValueDTO;
import com.nthuy.demo_erm.entity.RiskLineValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskLineValueMapper {

    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    RiskLineValueDTO toDto(RiskLineValueEntity entity);

    @Mapping(source = "attributeValue.id", target = "attributeValueId")
    RiskLineValueEntity toEntity(RiskLineValueDTO dto);
}
