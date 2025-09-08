package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.SampleActionDTO;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeResponse;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.SampleActionEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleActionMapper {

    // --- Entity -> DTO ---
    @Mapping(source = "classifyReasonId", target = "classifyReason")
    @Mapping(source = "riskTypeId", target = "riskType")
    SampleActionDTO toDto(SampleActionEntity entity);

    // --- DTO -> Entity ---
    @Mapping(source = "classifyReason.id", target = "classifyReasonId")
    @Mapping(source = "riskType.id", target = "riskTypeId")
    SampleActionEntity toEntity(SampleActionDTO dto);

    // --- Collections ---
    List<SampleActionDTO> toDtoList(List<SampleActionEntity> entities);
    List<SampleActionEntity> toEntityList(List<SampleActionDTO> dtos);

    // --- Update helper ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "classifyReason.id", target = "classifyReasonId")
    @Mapping(source = "riskType.id", target = "riskTypeId")
    void updateEntityFromDto(SampleActionDTO dto, @MappingTarget SampleActionEntity entity);

    // --- Custom mapping ---
    default ClassifyReasonResponse mapClassify(Long classifyReasonId) {
        if (classifyReasonId == null) return null;
        return new ClassifyReasonResponse(classifyReasonId, null, null);
    }

    default RiskTypeResponse mapRiskType(Long riskTypeId) {
        if (riskTypeId == null) return null;
        return new RiskTypeResponse(riskTypeId, null, null);
    }
}
