package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.HandlingMeasureDTO;
import com.nthuy.demo_erm.entity.HandlingMeasureEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HandlingMeasureMapper {

    HandlingMeasureDTO toDto(HandlingMeasureEntity entity);
    HandlingMeasureEntity toEntity(HandlingMeasureDTO dto);

    List<HandlingMeasureDTO> toDtoList(List<HandlingMeasureEntity> entities);
    List<HandlingMeasureEntity> toEntityList(List<HandlingMeasureDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(HandlingMeasureDTO dto, @MappingTarget HandlingMeasureEntity entity);

}
