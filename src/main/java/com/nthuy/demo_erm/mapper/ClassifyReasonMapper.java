package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassifyReasonMapper {

        @Mapping(source = "systemEntities", target = "systems")
        ClassifyReasonDTO toDto(ClassifyReasonEntity entity);

        @Mapping(source = "systems", target = "systemEntities")
        ClassifyReasonEntity toEntity(ClassifyReasonDTO dto);


//        ClassifyReasonDTO toDto(ClassifyReasonEntity entity);
//        ClassifyReasonEntity toEntity(ClassifyReasonDTO dto);

        List<ClassifyReasonDTO> toDtoList(List<ClassifyReasonEntity> entities);
        List<ClassifyReasonEntity> toEntityList(List<ClassifyReasonDTO> dtos);

        void updateEntityFromDto(ClassifyReasonDTO dto, @MappingTarget ClassifyReasonEntity entity);
}