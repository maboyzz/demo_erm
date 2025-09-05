package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.RiskCategoryService;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risk-category")
@RequiredArgsConstructor
public class RiskCategoryController {

    private final RiskCategoryService riskCategoryService;

    @PostMapping
    @ApiMessage("Tạo mới danh mục rủi ro")
    public ResponseEntity<IdResponse> createRiskCategory(@Valid @RequestBody RiskCategoryDTO dto) throws NameExisted {
        long newId = riskCategoryService.create(dto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin danh mục rủi ro theo id")
    public ResponseEntity<RiskCategoryDTO> getDetailsRiskCategory(@RequestParam Long id) {
        return ResponseUtils.ok(riskCategoryService.getRiskCategory(id));

    }

    @DeleteMapping
    @ApiMessage("Xóa danh mục rủi ro")
    public ResponseEntity<Void> deleteReason(@RequestParam Long id) {
        riskCategoryService.gelete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật danh mục rủi ro")
    public ResponseEntity<IdResponse> updateRiskCategory(@Valid @RequestBody RiskCategoryDTO dto) throws NameExisted {
        long newId = riskCategoryService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    public ResponseEntity<ResultPaginationDTO<RiskCategoryDTO>> getListRiskCategory(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)
        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(riskCategoryService.getListRiskCategory(code, name, systemIds, isActive, pageable));
    }
}
