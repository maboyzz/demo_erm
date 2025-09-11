package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.common.dto.IdResponse;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PageableUtils;
import com.nthuy.demo_erm.common.until.ResponseUtils;
import com.nthuy.demo_erm.common.until.annotation.ApiMessage;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskTypeDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import com.nthuy.demo_erm.dto.response.RiskTypeRes;
import com.nthuy.demo_erm.service.TrackingReasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracking-reason")
public class TrackingReasonController {

    private final TrackingReasonService trackingReasonService;

    @PostMapping
    @ApiMessage("Tạo mới")
    public ResponseEntity<IdResponse> createTrackingReason(@Valid @RequestBody TrackingReasonDTO dto) throws NameExisted {
        long newId = trackingReasonService.create(dto);
        return ResponseUtils.created(new IdResponse(newId));
    }

    @GetMapping
    @ApiMessage("Lấy thông tin ")
    public ResponseEntity<TrackingReasonDTO> getDetailsTrackingReason(@RequestParam Long id) {
        return ResponseUtils.ok(trackingReasonService.getTrackingReason(id));

    }

    @DeleteMapping
    @ApiMessage("Xóa danh mục rủi ro")
    public ResponseEntity<Void> deleteReason(@RequestParam Long id) {
        trackingReasonService.delete(id);
        return ResponseUtils.noContent();
    }

    @PutMapping
    @ApiMessage("Cập Nhật danh mục rủi ro")
    public ResponseEntity<IdResponse> updateTrackingReason(@Valid @RequestBody TrackingReasonDTO dto) throws NameExisted {
        long newId = trackingReasonService.update(dto);
        return ResponseUtils.ok(new IdResponse(newId));
    }

    @GetMapping("/list")
    @ApiMessage("list danh mục rủi ro")
    public ResponseEntity<ResultPaginationDTO<TrackingReasonDTO>> getListTrackingReason(
            @RequestParam(required = false) Set<Long> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        // Tạo Pageable từ param sort (vd: id,desc)
        Pageable pageable = PageableUtils.from(page, size, sort);
        return ResponseUtils.ok(trackingReasonService.getListTrackingReason(ids, pageable));
    }
}
