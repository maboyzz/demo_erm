package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskTypeDTO;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import com.nthuy.demo_erm.service.RiskTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risk-type")
@RequiredArgsConstructor
public class RiskTypeController {

    private final RiskTypeService riskTypeService;

    @PostMapping
    @ApiMessage("Tạo mới danh mục rủi ro")
    public ResponseEntity<IdResponse> createRiskCategory(@Valid @RequestBody RiskTypeDTO dto) throws NameExisted {
        long newId = riskTypeService.create(dto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin danh mục rủi ro theo id")
    public ResponseEntity<RiskTypeDTO> getDetailsRiskCategory(@RequestParam Long id) {
        return ResponseUtils.ok(riskTypeService.getRiskType(id));

    }

    @DeleteMapping
    @ApiMessage("Xóa danh mục rủi ro")
    public ResponseEntity<Void> deleteReason(@RequestParam Long id) {
        riskTypeService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật danh mục rủi ro")
    public ResponseEntity<IdResponse> updateRiskCategory(@Valid @RequestBody RiskTypeDTO dto) throws NameExisted {
        long newId = riskTypeService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list danh mục rủi ro")
    public ResponseEntity<ResultPaginationDTO<RiskTypeRes>> getListRiskCategory(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)
        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(riskTypeService.getListRiskType(code, name, systemIds, isActive, pageable));
    }
}
