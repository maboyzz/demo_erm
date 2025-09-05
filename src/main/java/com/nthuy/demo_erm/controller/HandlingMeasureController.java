package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;

import com.nthuy.demo_erm.dto.HandlingMeasureDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.service.HandlingMeasureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/handling-measure")
@RequiredArgsConstructor
public class HandlingMeasureController {
    private final HandlingMeasureService handlingMeasureService;

    @PostMapping
    @ApiMessage("Tạo mới biện pháp phòng ngừa")
    public ResponseEntity<IdResponse> createHandlingMeasure(@Valid @RequestBody HandlingMeasureDTO handlingMeasureDTO) throws NameExisted {
        long newId = handlingMeasureService.create(handlingMeasureDTO);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin biện pháp phòng ngừa theo id")
    public ResponseEntity<HandlingMeasureDTO> getDetailsHandlingMeasure(@RequestParam Long id) {
        return ResponseUtils.ok(handlingMeasureService.getHandlingMeasure(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa biện pháp phòng ngừa")
    public ResponseEntity<Void> deleteHandlingMeasure(@RequestParam Long id) {
        handlingMeasureService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập nhật biện pháp phòng ngừa")
    public ResponseEntity<IdResponse> updateHandlingMeasure(@Valid @RequestBody HandlingMeasureDTO handlingMeasureDTO) throws NameExisted {
        long newId = handlingMeasureService.update(handlingMeasureDTO);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list biện pháp phòng ngừa")
    public ResponseEntity<ResultPaginationDTO<HandlingMeasureDTO>> getHandlingMeasures(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(handlingMeasureService.getListHandlingMeasure(code, name, isActive, pageable));
    }
}
