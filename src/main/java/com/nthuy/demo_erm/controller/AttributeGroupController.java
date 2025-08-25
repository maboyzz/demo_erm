package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.AttributeGroupService;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttributeGroupController {

    private final AttributeGroupService attributeGroupService;


    public AttributeGroupController(AttributeGroupService attributeGroupService) {
        this.attributeGroupService = attributeGroupService;
    }

    @PostMapping("/api/v1/attribute-group")
    @ApiMessage("Tạo mới nhóm thuộc tính")
    public ResponseEntity<IdResponse> createAttributeGroup(
            @Valid
            @RequestBody AttributeGroupDTO dto
    ) throws NameExisted {
        boolean nameExists = this.attributeGroupService.nameExists(dto.getName());
        if (nameExists) {
            throw new NameExisted("Username " +
                    dto.getName() + " đã tồn tại");
        }
        long newId = attributeGroupService.handleCreateAttributeGroup(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

}
