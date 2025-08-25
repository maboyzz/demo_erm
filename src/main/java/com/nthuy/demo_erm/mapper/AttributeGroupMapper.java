package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttributeGroupMapper {

    AttributeGroupDTO toDto(AttributeGroupEntity entity);

    AttributeGroupEntity toEntity(AttributeGroupDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AttributeGroupDTO dto, @MappingTarget AttributeGroupEntity entity);

    // Collections
    List<AttributeGroupDTO> toDtoList(List<AttributeGroupEntity> entities);
}
