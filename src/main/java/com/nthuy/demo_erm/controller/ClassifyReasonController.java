package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.ClassifyReasonService;
import com.nthuy.demo_erm.until.PageableUtils;
import com.nthuy.demo_erm.until.ResponseUtils;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classify-reason")
@RequiredArgsConstructor
public class ClassifyReasonController {

    private final ClassifyReasonService classifyReasonService;

    @PostMapping
    @ApiMessage("Tạo mới phân loại nguyên nhân")
    public ResponseEntity<IdResponse> createClassifyReason(@Valid @RequestBody ClassifyReasonDTO classifyReasonDTO) throws NameExisted {
        long newId = classifyReasonService.create(classifyReasonDTO);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin phân loại nguyên nhân theo id")
    public ResponseEntity<ClassifyReasonDTO> getDetailsClassifyReason(@RequestParam Long id) {
        return ResponseUtils.ok(classifyReasonService.getClassifyReason(id));
    }

    @DeleteMapping
    @ApiMessage("Xóa phân loại nguyên nhân")
    public ResponseEntity<Void> deleteClassifyReason(@RequestParam Long id) {
        classifyReasonService.delete(id);
        return ResponseUtils.noContent();
    }

    @GetMapping("/list")
    public ResponseEntity<ResultPaginationDTO<ClassifyReasonDTO>> getClassifyReasons(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(classifyReasonService.getListClassifyReason(code, name, systemIds, pageable));
    }

    @PutMapping
    @ApiMessage("Cập nhật phân loại nguyên nhân")
    public ResponseEntity<IdResponse> updateClassifyReason(@Valid @RequestBody ClassifyReasonDTO classifyReasonDTO) throws NameExisted {
        long newId = classifyReasonService.update(classifyReasonDTO);
        return ResponseUtils.ok(new IdResponse(newId));
    }
}