package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.dto.AttributeValueDTO;
import com.nthuy.demo_erm.entity.AttributeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = { AttributeValueMapper.class })
public interface AttributeMapper {

    // Map cơ bản
    AttributeDTO toDto(AttributeEntity entity);
    AttributeEntity toEntity(AttributeDTO dto);

    // Collection helpers (tuỳ nhu cầu, MapStruct cũng tự infer nhưng khai rõ cho clarity)
    List<AttributeDTO> toDtoList(List<AttributeEntity> entities);
    List<AttributeEntity> toEntityList(List<AttributeDTO> dtos);

    // Map giữa collection types (List <-> Set)
    Set<AttributeValueDTO> toValueDtoSet(Set<com.nthuy.demo_erm.entity.AttributeValueEntity> entities);
    Set<com.nthuy.demo_erm.entity.AttributeValueEntity> toValueEntitySet(Set<com.nthuy.demo_erm.dto.AttributeValueDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AttributeDTO dto, @MappingTarget AttributeEntity entity);

}