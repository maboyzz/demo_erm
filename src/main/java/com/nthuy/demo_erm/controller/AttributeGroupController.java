package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.AttributeGroupService;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attribute-group")
@RequiredArgsConstructor
public class AttributeGroupController {

    private final AttributeGroupService attributeGroupService;

    @PostMapping
    @ApiMessage("Tạo mới nhóm thuộc tính")
    public ResponseEntity<IdResponse> createAttributeGroup(@Valid @RequestBody AttributeGroupDTO dto) throws NameExisted {
        long newId = attributeGroupService.create(dto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin nhóm thuộc tính theo id")
    public ResponseEntity<AttributeGroupDTO> getDetailsAttributeGroup(@RequestParam Long id) {
        return ResponseUtils.ok(attributeGroupService.getAttributeGroup(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa nhóm thuộc tính")
    public ResponseEntity<Void> deleteAttributeGroup(@RequestParam Long id) {
        attributeGroupService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật nhóm thuộc tính")
    public ResponseEntity<IdResponse> updateAttributeGroup(@Valid @RequestBody AttributeGroupDTO dto) throws NameExisted {
        long newId = attributeGroupService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list nhóm thuộc tính")
    public ResponseEntity<ResultPaginationDTO<AttributeGroupDTO>> getAttributeGroups(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)
        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(attributeGroupService.getListAttributeGroup(SearchRequest.builder()
                .code(code)
                .name(name)
                .isActive(isActive)
                .build(), pageable));
    }
}
