package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.RiskCategoryService;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RiskCategoryController {

    private final RiskCategoryService riskCategoryService;


    public RiskCategoryController(RiskCategoryService riskCategoryService) {
        this.riskCategoryService = riskCategoryService;
    }

    @PostMapping("/api/v1/risk-category")
    @ApiMessage("Tạo mới danh mục rủi ro")
    public ResponseEntity<IdResponse> createRiskCategory(
            @Valid
            @RequestBody RiskCategoryDTO dto
    ) throws NameExisted {
        boolean nameExists = this.riskCategoryService.nameExists(dto.getName());
        if (nameExists) {
            throw new NameExisted("Name " +
                    dto.getName() + " đã tồn tại");
        }
        boolean isValidId = riskCategoryService.existsById(dto.getParent_id());
        if (!isValidId) {
            throw new IdInvalidException("ID danh mục cha " + dto.getParent_id() + " không có");
        }
        long newId = riskCategoryService.handleCreateRiskCategory(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }
    @GetMapping(value = "/api/v1/risk-category", params = "id")
    @ApiMessage("Lấy thông tin danh mục rủi ro theo id")
    public ResponseEntity<RiskCategoryDTO> getDetailsRiskCategory(
            @RequestParam Long id
    ) {
        RiskCategoryDTO dto = riskCategoryService.handleGetRiskCategoryById(id);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping(value = "/api/v1/risk-category", params = "id")
    @ApiMessage("Xóa danh mục rủi ro")
    public ResponseEntity<String> deleteReason(
            @RequestParam Long id
    ) {
        boolean isValidId = riskCategoryService.existsById(id);
        if (!isValidId) {
            throw new IdInvalidException("ID " + id + " không có");
        }
        this.riskCategoryService.handleDeleteRiskCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xoá thành công");
    }
    @PutMapping("/api/v1/risk-category")
    @ApiMessage("Cập Nhật danh mục rủi ro")
    public ResponseEntity<IdResponse> updateRiskCategory(
            @Valid
            @RequestBody RiskCategoryDTO dto
    ) throws NameExisted {
        boolean nameExists = this.riskCategoryService.nameExists(dto.getName());
        boolean isValidId = riskCategoryService.existsById(dto.getParent_id());

        if (nameExists) {
            throw new NameExisted("Username " +
                    dto.getName() + " đã tồn tại");
        }
        if (!isValidId) {
            throw new IdInvalidException("ID danh mục cha " + dto.getParent_id() + " không có");
        }
        long newId = riskCategoryService.handleUpdateRiskCategory(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }


}
