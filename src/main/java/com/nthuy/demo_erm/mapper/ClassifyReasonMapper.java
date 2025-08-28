package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SystemMapper.class})
public interface ClassifyReasonMapper {


    ClassifyReasonDTO toDto(ClassifyReasonEntity entity);

    ClassifyReasonEntity toEntity(ClassifyReasonDTO dto);

    List<ClassifyReasonDTO> toDtoList(List<ClassifyReasonEntity> entities);

    List<ClassifyReasonEntity> toEntityList(List<ClassifyReasonDTO> dtos);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClassifyReasonDTO dto, @MappingTarget ClassifyReasonEntity entity);


}