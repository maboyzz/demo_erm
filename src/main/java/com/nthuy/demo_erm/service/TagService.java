package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface TagService {

    Long create (TagDTO dto);

    ResultPaginationDTO<TagDTO> getListTag(Set<Long> ids, Pageable pageable);
}
