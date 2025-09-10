package com.nthuy.demo_erm.mapper;

import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.TagEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toDto(TagEntity entity);
    TagEntity toEntity(TagDTO dto);


    List<TagDTO> toDtoList(List<TagEntity> entities);
    List<TagEntity> toEntityList(List<TagDTO> dtos);
}
