package com.nthuy.demo_erm.controller;


import com.nthuy.demo_erm.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.ReasonService;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReasonController {


    private final ReasonService reasonService;

    public ReasonController(ReasonService reasonService) {
        this.reasonService = reasonService;
    }

    @PostMapping("/api/v1/reason")
    @ApiMessage("Tạo mới nguyên nhân")
    public ResponseEntity<IdResponse> createReason(
            @Valid
            @RequestBody ReasonDTO reasonDTO
    ) throws NameExisted {
        boolean nameExists = this.reasonService.nameExists(reasonDTO.getName());
        if (nameExists) {
            throw new NameExisted("Username " +
                    reasonDTO.getName() + " đã tồn tại");
        }
        long newId = reasonService.handleCreateClassifyReason(reasonDTO);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }


    @GetMapping(value = "/api/v1/reason", params = "id")
    @ApiMessage("Lấy thông tin nguyên nhân theo id")
    public ResponseEntity<ReasonDTO> getDetailsReason(
            @RequestParam Long id
    ) {
        ReasonDTO dto = reasonService.handleGetReasonById(id);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping(value = "/api/v1/reason", params = "id")
    @ApiMessage("Xóa nguyên nhân")
    public ResponseEntity<String> deleteReason(
            @RequestParam Long id
    ) {
        boolean isValidId = reasonService.existsById(id);
        if (!isValidId) {
            throw new IdInvalidException("ID " + id + " không hợp có");
        }
        this.reasonService.handleDeleteReason(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xoá thành công");
    }
    @PutMapping("/api/v1/reason")
    @ApiMessage("Cập Nhật phân loại nguyên nhân")
    public ResponseEntity<IdResponse> updateReason(
            @Valid
            @RequestBody ReasonDTO dto
    ) throws NameExisted {
        boolean nameExists = this.reasonService.nameExists(dto.getName());
        if (nameExists) {
            throw new NameExisted("Username " +
                    dto.getName() + " đã tồn tại");
        }
        long newId = reasonService.handleUpdateReason(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

    @GetMapping("/api/v1/reason")
    public ResponseEntity<ResultPaginationDTO<ReasonDTO>> getReasons(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) EnumTypeReason type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        // Tạo Pageable từ param sort (vd: id,desc)
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        ResultPaginationDTO<ReasonDTO> result =
                reasonService.handleGetReason(code, name, systemIds, isActive, type, pageable);

        return ResponseEntity.ok(result);
    }
}
