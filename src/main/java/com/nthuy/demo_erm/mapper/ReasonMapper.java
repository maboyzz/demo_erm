package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.entity.ReasonEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

// Uses SystemMapper to convert nested SystemEntity <-> SystemDTO
@Mapper(componentModel = "spring", uses = { SystemMapper.class })
public interface ReasonMapper {

    // ReasonDTO: Set<SystemDTO> systems
    // ReasonEntity: Set<SystemEntity> systemEntitiesReason
    @Mapping(source = "systemEntitiesReason", target = "systems")
    ReasonDTO toDto(ReasonEntity entity);

    @Mapping(source = "systems", target = "systemEntitiesReason")
    ReasonEntity toEntity(ReasonDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "systems", target = "systemEntitiesReason")
    void updateEntityFromDto(ReasonDTO dto, @MappingTarget ReasonEntity entity);

    // Collections
    List<ReasonDTO> toDtoList(List<ReasonEntity> entities);
    List<ReasonEntity> toEntityList(List<ReasonDTO> dtos);

    Set<ReasonDTO> toDtoSet(Set<ReasonEntity> entities);
    Set<ReasonEntity> toEntitySet(Set<ReasonDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "systemEntitiesReason", ignore = true)
    void updateEntityCoreFields(ReasonDTO dto, @MappingTarget ReasonEntity entity);
}