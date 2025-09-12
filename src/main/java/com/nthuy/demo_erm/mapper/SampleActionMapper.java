package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.SampleActionDTO;
import com.nthuy.demo_erm.entity.SampleActionEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleActionMapper {

    // --- Entity -> DTO ---
    @Mapping(source = "classifyReasonId", target = "classifyReason.id")
    @Mapping(source = "riskTypeId", target = "riskType.id")
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
}
