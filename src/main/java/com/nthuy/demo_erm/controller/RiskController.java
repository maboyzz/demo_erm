package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskDTO;
import com.nthuy.demo_erm.service.RiskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/risk")
@RequiredArgsConstructor
public class RiskController {
    private final RiskService riskService;

    @PostMapping
    @ApiMessage("Tạo mới ")
    public ResponseEntity<IdResponse> createRisk(@Valid @RequestBody RiskDTO riskDTO) throws NameExisted {
        long newId = riskService.create(riskDTO);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin  id")
    public ResponseEntity<RiskDTO> getDetailsRisk(@RequestParam Long id) {
        return ResponseUtils.ok(riskService.getRisk(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa")
    public ResponseEntity<Void> deleteRisk(@RequestParam Long id) {
        riskService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật phân loại")
    public ResponseEntity<IdResponse> updateRisk(@Valid @RequestBody RiskDTO dto) throws NameExisted {
        long newId = riskService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list")
    public ResponseEntity<ResultPaginationDTO<RiskDTO>> getRisks(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) EnumTypeReason type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(riskService.getListRisk(code, name, systemIds, isActive, type, pageable));

    }
}
