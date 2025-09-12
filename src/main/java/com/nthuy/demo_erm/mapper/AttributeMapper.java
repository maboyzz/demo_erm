package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.dto.AttributeValueDTO;
import com.nthuy.demo_erm.dto.response.AttributeGroupResponse;
import com.nthuy.demo_erm.dto.response.AttributeResponse;
import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.AttributeValueEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = { AttributeValueMapper.class })
public interface AttributeMapper {

    // --- Entity -> DTO ---

    @Mapping(source = "attributeGroupId", target = "attributeGroup.id")
    AttributeDTO toDto(AttributeEntity entity);

    AttributeResponse toDtoRes(AttributeEntity entity);

    // --- DTO -> Entity ---
    @Mapping(source = "attributeGroup.id", target = "attributeGroupId")
    AttributeEntity toEntity(AttributeDTO dto);


    AttributeEntity toEntityRes(AttributeResponse dto);
    // --- Collections ---
    List<AttributeDTO> toDtoList(List<AttributeEntity> entities);
    List<AttributeEntity> toEntityList(List<AttributeDTO> dtos);

    Set<AttributeValueDTO> toValueDtoSet(Set<AttributeValueEntity> entities);
    Set<AttributeValueEntity> toValueEntitySet(Set<AttributeValueDTO> dtos);

    // --- Update helper ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "attributeGroup.id", target = "attributeGroupId")
    void updateEntityFromDto(AttributeDTO dto, @MappingTarget AttributeEntity entity);

}