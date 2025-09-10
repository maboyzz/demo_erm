package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.RiskFileDTO;
import com.nthuy.demo_erm.entity.RiskFileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface RiskFileMapper {
    RiskFileDTO toDto(RiskFileEntity entity);
    RiskFileEntity toEntity(RiskFileDTO dto);

}
