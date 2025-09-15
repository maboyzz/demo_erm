package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import com.nthuy.demo_erm.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ApiMessage("Tạo mới")
    public ResponseEntity<IdResponse> createTag(@Valid @RequestBody TagDTO tagDTO) throws NameExisted {
        long newId = tagService.create(tagDTO);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list")
    public ResponseEntity<ResultPaginationDTO<TagDTO>> getRisks(
            @RequestParam(required = false) Set<Long> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(tagService.getListTag(ids, pageable));
    }
}
