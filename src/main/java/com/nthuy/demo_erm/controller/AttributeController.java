package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.service.AttributeService;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attribute")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping
    @ApiMessage("Tạo mới thuộc tính")
    public ResponseEntity<IdResponse> createAttribute(@Valid @RequestBody AttributeDTO dto) throws NameExisted {
        long newId = attributeService.create(dto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin thuộc tính theo id")
    public ResponseEntity<AttributeDTO> getDetailsAttribute(@RequestParam Long id) {
        return ResponseUtils.ok(attributeService.getAttribute(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa thuộc tính")
    public ResponseEntity<Void> deleteAttribute(@RequestParam Long id) {
        attributeService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật thuộc tính")
    public ResponseEntity<IdResponse> updateAttribute(@Valid @RequestBody AttributeDTO dto) throws NameExisted {
        long newId = attributeService.update(dto);
       return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list thuộc tính")
    public ResponseEntity<ResultPaginationDTO<AttributeDTO>> getAttributes(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Long attributeGroupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(attributeService.getListAttribute(SearchRequest.builder()
                .code(code)
                .name(name)
                .isActive(isActive)
                .attributeGroupId(attributeGroupId)
                .build(), pageable));
    }

}
