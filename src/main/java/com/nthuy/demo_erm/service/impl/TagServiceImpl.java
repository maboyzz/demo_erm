package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.constant.EnumAttributeDisplayType;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.AttributeSpecification;
import com.nthuy.demo_erm.config.TagSpecification;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TagDTO;


import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.TagEntity;
import com.nthuy.demo_erm.mapper.TagMapper;
import com.nthuy.demo_erm.repository.TagRepository;
import com.nthuy.demo_erm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;


    @Override
    public Long create(TagDTO dto) {
        TagEntity entity = tagMapper.toEntity(dto);
        return tagRepository.saveAndFlush(entity).getId();
    }

    @Override
    public ResultPaginationDTO<TagDTO> getListTag(Set<Long> ids, Pageable pageable) {
        Specification<TagEntity> spec = Specification.where(null);

        // thêm điều kiện filter theo ids nếu có
        spec = spec.and(TagSpecification.hasIds(ids));

        Page<TagEntity> pageResult = tagRepository.findAll(spec, pageable);

        List<TagDTO> dtoList = pageResult.isEmpty()
                ? Collections.emptyList()
                : tagMapper.toDtoList(pageResult.getContent());

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }
}
