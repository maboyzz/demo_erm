package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.RiskTrackingReasonDTO;
import com.nthuy.demo_erm.entity.RiskTrackingReasonEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RiskTrackingReasonMapper {
    RiskTrackingReasonDTO toDto(RiskTrackingReasonEntity entity);
    List<RiskTrackingReasonDTO> toDtoList(List<RiskTrackingReasonEntity> entities);
}