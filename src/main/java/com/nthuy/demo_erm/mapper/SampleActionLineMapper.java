package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.SampleActionLineDTO;
import com.nthuy.demo_erm.entity.SampleActionLineEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleActionLineMapper {

    // entity.name → dto.handlingMeasureName.name
    @Mapping(source = "handlingMeasureId", target = "handlingMeasure.id")
    SampleActionLineDTO toDto(SampleActionLineEntity entity);

    // dto.handlingMeasureName.name → entity.name
    @Mapping(source = "handlingMeasure.id", target = "handlingMeasureId")
    SampleActionLineEntity toEntity(SampleActionLineDTO dto);

    // list mapping không cần khai báo Mapping nữa (MapStruct tự áp dụng rule trên)
    List<SampleActionLineDTO> toDtoList(List<SampleActionLineEntity> entities);

    List<SampleActionLineEntity> toEntityList(List<SampleActionLineDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SampleActionLineDTO dto, @MappingTarget SampleActionLineEntity entity);
}
