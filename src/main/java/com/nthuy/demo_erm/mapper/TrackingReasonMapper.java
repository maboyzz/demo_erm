package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingReasonMapper {

    @Mapping(source = "classifyReasonId", target = "classifyReason.id")
    @Mapping(source = "reasonId", target = "reason.id")
    TrackingReasonDTO toDto(TrackingReasonEntity entity);

    @Mapping(source = "reason.id", target = "reasonId")
    @Mapping(source = "classifyReason.id", target = "classifyReasonId")
    TrackingReasonEntity toEntity(TrackingReasonDTO dto);


    @Mapping(source = "classifyReasonId", target = "classifyReason.id")
    @Mapping(source = "reasonId", target = "reason.id")
    List<TrackingReasonDTO> toDtoList(List<TrackingReasonEntity> entity);
}
