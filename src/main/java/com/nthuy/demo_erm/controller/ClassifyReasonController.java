package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.ClassifyReasonService;
import com.nthuy.demo_erm.service.ClassifyReasonServiceImpl;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClassifyReasonController {

    private final ClassifyReasonService classifyReasonService;

    public ClassifyReasonController(ClassifyReasonServiceImpl classifyReasonServiceImpl) {
        this.classifyReasonService = classifyReasonServiceImpl;
    }


    @PostMapping("/api/v1/classify-reason")
    @ApiMessage("Tạo mới phân loại nguyên nhân")
    public ResponseEntity<IdResponse> createClassifyReason(
            @Valid
            @RequestBody ClassifyReasonDTO classifyReasonDTO
    ) throws NameExisted {
            boolean nameExists = this.classifyReasonService.nameExists(classifyReasonDTO.getName());
            if (nameExists) {
                throw new NameExisted("Username " +
                        classifyReasonDTO.getName() + " đã tồn tại");
            }
        long newId = classifyReasonService.handleCreateClassifyReason(classifyReasonDTO);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }


    @GetMapping(value = "/api/v1/classify-reason", params = "id")
    @ApiMessage("Lấy thông tin phân loại nguyên nhân theo id")
    public ResponseEntity<ClassifyReasonDTO> getDetailsClassifyReason(
            @RequestParam Long id
    ) {
        ClassifyReasonDTO dto = classifyReasonService.handleGetClassifyReasonById(id);
        return ResponseEntity.ok(dto);
    }


//    @GetMapping("/api/v1/classify-reason")
//    @ApiMessage("list phân loại nguyên nhân")
//    public ResponseEntity<List<ClassifyReasonDTO>> getClassifyReason(
//    ) {
//        return ResponseEntity.ok(this.classifyReasonService.handleGetClassifyReason());
//    }


    @DeleteMapping(value = "/api/v1/classify-reason", params = "id")
    @ApiMessage("Xóa phân loại nguyên nhân")
    public ResponseEntity<String> deleteClassifyReason(
            @RequestParam Long id
    ) {
        boolean isValidId = classifyReasonService.existsById(id);
        if (!isValidId) {
            throw new IdInvalidException("ID " + id + " không hợp có");
        }
        this.classifyReasonService.handleDeleteClassifyReason(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xoá thành công");
    }

    @GetMapping("/api/v1/classify-reason")
    public ResponseEntity<List<ClassifyReasonDTO>> getClassifyReasons(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> systemIds
    ) {
        List<ClassifyReasonDTO> list = classifyReasonService.handleGetClassifyReason(code, name, systemIds);
        return ResponseEntity.ok(list);
    }
}
