package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.entity.ReasonEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReasonMapper {

    // --- Entity -> DTO ---
    @Mapping(source = "classifyReasonId", target = "classifyReason.id")
    ReasonDTO toDto(ReasonEntity entity);

    // --- DTO -> Entity ---
    @Mapping(source = "classifyReason.id", target = "classifyReasonId")
    ReasonEntity toEntity(ReasonDTO dto);

    // --- Collections ---
    List<ReasonDTO> toDtoList(List<ReasonEntity> entities);
    List<ReasonEntity> toEntityList(List<ReasonDTO> dtos);

    // --- Update helper ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "classifyReason.id", target = "classifyReasonId")
    void updateEntityFromDto(ReasonDTO dto, @MappingTarget ReasonEntity entity);


}