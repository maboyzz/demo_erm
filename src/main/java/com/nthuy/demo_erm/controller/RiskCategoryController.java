package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.RiskCategoryService;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        long newId = riskCategoryService.handleCreateRiskCategory(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

}
