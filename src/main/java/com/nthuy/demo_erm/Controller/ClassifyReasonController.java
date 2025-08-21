package com.nthuy.demo_erm.Controller;

import com.nthuy.demo_erm.DTO.ClassifyReasonDTO;
import com.nthuy.demo_erm.DTO.IdResponse;
import com.nthuy.demo_erm.Exception.IdInvalidException;
import com.nthuy.demo_erm.Exception.NameExisted;
import com.nthuy.demo_erm.Service.ClassifyReasonService;
import com.nthuy.demo_erm.Until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClassifyReasonController {

    private final ClassifyReasonService classifyReasonService;

    public ClassifyReasonController(ClassifyReasonService classifyReasonService) {
        this.classifyReasonService = classifyReasonService;
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
    @ApiMessage("Lấy thông tin phân loại nguyên nhân")
    public ResponseEntity<ClassifyReasonDTO> getDetailsClassifyReason(
            @RequestParam Long id
    ) {
        ClassifyReasonDTO dto = classifyReasonService.handleGetClassifyReasonById(id);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/api/v1/classify-reason")
    @ApiMessage("list phân loại nguyên nhân")
    public ResponseEntity<List<ClassifyReasonDTO>> getClassifyReason(
    ) {
        return ResponseEntity.ok(this.classifyReasonService.handleGetClassifyReason());
    }


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
}
