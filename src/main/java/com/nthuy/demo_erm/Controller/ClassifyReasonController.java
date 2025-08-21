package com.nthuy.demo_erm.Controller;

import com.nthuy.demo_erm.DTO.ClassifyReasonDTO;
import com.nthuy.demo_erm.Exception.NameExisted;
import com.nthuy.demo_erm.Service.ClassifyReasonService;
import com.nthuy.demo_erm.Until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClassifyReasonController {
    private final ClassifyReasonService classifyReasonService;

    public ClassifyReasonController(ClassifyReasonService classifyReasonService) {
        this.classifyReasonService = classifyReasonService;
    }


    @PostMapping("/api/v1/classify-reason")
    @ApiMessage("Tạo mới phân loại nguyên nhân")
    public ResponseEntity<Long> createClassifyReason(
            @Valid
            @RequestBody ClassifyReasonDTO classifyReasonDTO
    ) throws NameExisted {
            boolean nameExists = this.classifyReasonService.nameExists(classifyReasonDTO.getName());
            if (nameExists) {
                throw new NameExisted("Username " +
                        classifyReasonDTO.getName() + " đã tồn tại");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(this.classifyReasonService.handleCreateClassifyReason(classifyReasonDTO));

    }
    @GetMapping("/api/v1/classify-reason")
    @ApiMessage("Lấy thông tin phân loại nguyên nhân")
    public ResponseEntity<ClassifyReasonDTO> getHealthInsuranceCardById(
            @RequestParam Long id
    ) {
        ClassifyReasonDTO dto = classifyReasonService.handleGetById(id);
        return ResponseEntity.ok(dto);
    }
}
