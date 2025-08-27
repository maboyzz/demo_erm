package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.entity.AttributeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
    AttributeDTO toDto(AttributeEntity entity);
    AttributeEntity toEntity(AttributeDTO dto);
}