package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.TrackingActionDTO;
import com.nthuy.demo_erm.entity.TrackingActionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface TrackingActionMapper {
    @Mapping(source = "handlingMeasureId", target = "handlingMeasure.id")
    TrackingActionDTO toDto(TrackingActionEntity entity);

    @Mapping(source = "handlingMeasure.id", target = "handlingMeasureId")
    TrackingActionEntity toEntity(TrackingActionDTO dto);

    List<TrackingActionEntity> toEntityList(List<TrackingActionDTO> dtos);
}
