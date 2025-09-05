package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RiskCategoryMapper {


    RiskCategoryDTO toDto(RiskCategoryEntity entity);


    RiskCategoryEntity toEntity(RiskCategoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RiskCategoryDTO dto, @MappingTarget RiskCategoryEntity entity);

    // Collections
    List<RiskCategoryDTO> toDtoList(List<RiskCategoryEntity> entities);
    List<RiskCategoryEntity> toEntityList(List<RiskCategoryDTO> dtos);

    Set<RiskCategoryDTO> toDtoSet(Set<RiskCategoryEntity> entities);
    Set<RiskCategoryEntity> toEntitySet(Set<RiskCategoryDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityCoreFields(RiskCategoryDTO dto, @MappingTarget RiskCategoryEntity entity);
}
