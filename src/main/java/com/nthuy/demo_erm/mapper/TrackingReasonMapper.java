package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackingReasonMapper {

    TrackingReasonDTO toDto(TrackingReasonEntity entity);
    TrackingReasonEntity toEntity(TrackingReasonDTO dto);
}
