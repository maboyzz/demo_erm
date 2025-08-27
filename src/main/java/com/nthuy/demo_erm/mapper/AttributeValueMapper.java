package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.AttributeValueDTO;
import com.nthuy.demo_erm.entity.AttributeValueEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    AttributeValueDTO toDto(AttributeValueEntity entity);
    AttributeValueEntity toEntity(AttributeValueDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
        // @Mapping(target = "attributeId", ignore = true)
    void updateEntityFromDto(AttributeValueDTO dto, @MappingTarget AttributeValueEntity entity);
}