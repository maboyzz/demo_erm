package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.SampleActionLineDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.SampleActionEntity;
import com.nthuy.demo_erm.entity.SampleActionLineEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleActionLineMapper {
    SampleActionLineDTO toDto(SampleActionLineEntity entity);

    SampleActionLineEntity toEntity(SampleActionLineDTO dto);

    List<SampleActionLineDTO> toDtoList(List<SampleActionLineEntity> entities);

    List<SampleActionLineEntity> toEntityList(List<SampleActionLineDTO> dtos);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SampleActionLineDTO dto, @MappingTarget SampleActionLineEntity entity);

}
