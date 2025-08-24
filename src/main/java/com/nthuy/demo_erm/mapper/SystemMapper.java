package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.SystemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemMapper {
    SystemDTO toDto(SystemEntity entity);
    SystemEntity toEntity(SystemDTO dto);
}