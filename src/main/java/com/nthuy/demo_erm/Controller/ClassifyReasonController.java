package com.nthuy.demo_erm.Controller;

import com.nthuy.demo_erm.DTO.ClassifyReasonDTO;
import com.nthuy.demo_erm.Exception.NameExisted;
import com.nthuy.demo_erm.Service.ClassifyReasonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassifyReasonController {
    private final ClassifyReasonService classifyReasonService;

    public ClassifyReasonController(ClassifyReasonService classifyReasonService) {
        this.classifyReasonService = classifyReasonService;
    }


    @PostMapping("/api/v1/classify-reason")
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
}
