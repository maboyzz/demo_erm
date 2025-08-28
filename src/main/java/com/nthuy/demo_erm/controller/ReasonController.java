package com.nthuy.demo_erm.controller;


import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.ReasonService;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reason")
@RequiredArgsConstructor
public class ReasonController {


    private final ReasonService reasonService;


    @PostMapping
    @ApiMessage("Tạo mới nguyên nhân")
    public ResponseEntity<IdResponse> createReason(@Valid @RequestBody ReasonDTO reasonDTO) throws NameExisted {
        long newId = reasonService.create(reasonDTO);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin nguyên nhân theo id")
    public ResponseEntity<ReasonDTO> getDetailsReason(@RequestParam Long id) {
        return ResponseUtils.ok(reasonService.getReason(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa nguyên nhân")
    public ResponseEntity<String> deleteReason(@RequestParam Long id) {
        reasonService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật phân loại nguyên nhân")
    public ResponseEntity<IdResponse> updateReason(@Valid @RequestBody ReasonDTO dto) throws NameExisted {
        long newId = reasonService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    public ResponseEntity<ResultPaginationDTO<ReasonDTO>> getReasons(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) EnumTypeReason type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(reasonService.getListReason(code, name, systemIds, isActive, type, pageable));


    }
}
