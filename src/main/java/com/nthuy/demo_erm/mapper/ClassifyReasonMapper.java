package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

// Sử dụng SystemMapper để ánh xạ Set<SystemEntity> <-> Set<SystemDTO>
@Mapper(componentModel = "spring", uses = { SystemMapper.class })
public interface ClassifyReasonMapper {

        @Mapping(source = "systemEntitiesClassifyReason", target = "systems")
        ClassifyReasonDTO toDto(ClassifyReasonEntity entity);

        @Mapping(source = "systems", target = "systemEntitiesClassifyReason")
        ClassifyReasonEntity toEntity(ClassifyReasonDTO dto);

        List<ClassifyReasonDTO> toDtoList(List<ClassifyReasonEntity> entities);
        List<ClassifyReasonEntity> toEntityList(List<ClassifyReasonDTO> dtos);

        // Update kiểu PATCH: bỏ qua null từ DTO, và vẫn map systems -> systemEntitiesClassifyReason
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(source = "systems", target = "systemEntitiesClassifyReason")
        void updateEntityFromDto(ClassifyReasonDTO dto, @MappingTarget ClassifyReasonEntity entity);

        // Tuỳ chọn: nếu muốn PATCH chỉ trường cơ bản, KHÔNG đụng tới quan hệ
        // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        // @Mapping(target = "systemEntitiesClassifyReason", ignore = true)
        // void updateEntityCoreFields(ClassifyReasonDTO dto, @MappingTarget ClassifyReasonEntity entity);
}