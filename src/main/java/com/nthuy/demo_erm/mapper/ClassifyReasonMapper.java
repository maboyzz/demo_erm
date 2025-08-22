package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassifyReasonMapper {

        ClassifyReasonDTO toDto(ClassifyReasonEntity entity);
        ClassifyReasonEntity toEntity(ClassifyReasonDTO dto);

}