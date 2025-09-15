package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SampleActionDTO;
import com.nthuy.demo_erm.service.SampleActionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sample-action")
@RequiredArgsConstructor
public class SampleActionController {
    private final SampleActionService sampleActionService;

    @PostMapping
    @ApiMessage("Tạo mới danh mục rủi ro")
    public ResponseEntity<IdResponse> createRiskCategory(@Valid @RequestBody SampleActionDTO sampleDto) throws NameExisted {
        long newId = sampleActionService.create(sampleDto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin danh mục rủi ro theo id")
    public ResponseEntity<SampleActionDTO> getDetailsRiskCategory(@RequestParam Long id) {
        return ResponseUtils.ok(sampleActionService.getSampleAction(id));

    }

    @DeleteMapping
    @ApiMessage("Xóa danh mục rủi ro")
    public ResponseEntity<Void> deleteSampleAction(@RequestParam Long id) {
        sampleActionService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật danh mục rủi ro")
    public ResponseEntity<IdResponse> updateSampleAction(@Valid @RequestBody SampleActionDTO dto) throws NameExisted {
        long newId = sampleActionService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list danh mục rủi ro")
    public ResponseEntity<ResultPaginationDTO<SampleActionDTO>> getListSampleAction(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long riskTypeId,
            @RequestParam(required = false) Long classifyReasonId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)
        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(sampleActionService.getListSampleAction(code, name, riskTypeId, classifyReasonId, isActive, pageable));
    }
}
