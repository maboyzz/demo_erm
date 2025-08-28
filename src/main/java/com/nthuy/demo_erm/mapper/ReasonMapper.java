package com.nthuy.demo_erm.mapper;


import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ReasonEntity;
import org.mapstruct.*;

import java.util.List;


// Uses SystemMapper to convert nested SystemEntity <-> SystemDTO
@Mapper(componentModel = "spring")
public interface ReasonMapper {


    @Mapping(target = "classifyReason.id", source = "classifyReasonId")
    ReasonDTO toDto(ReasonEntity entity);

    @Mapping(target = "classifyReasonId", source = "classifyReason.id")
    ReasonEntity toEntity(ReasonDTO dto);

    @Mapping(target = "classifyReasonId", source = "classifyReason.id")
    List<ReasonDTO> toDtoList(List<ReasonEntity> entities);

    List<ReasonEntity> toEntityList(List<ReasonDTO> dtos);

    @Mapping(target = "classifyReasonId", source = "classifyReason.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ReasonDTO dto, @MappingTarget ReasonEntity entity);

    default void setClassifyReason(ReasonDTO dto, ClassifyReasonEntity classify) {
        if (classify != null) {
            dto.setClassifyReason(
                    new ClassifyReasonResponse(
                            classify.getId(),
                            classify.getCode(),
                            classify.getName()
                    )
            );
        }
    }
}