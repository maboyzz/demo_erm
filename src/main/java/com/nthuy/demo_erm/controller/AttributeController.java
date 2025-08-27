package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.service.AttributeService;
import com.nthuy.demo_erm.until.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }


    @PostMapping("/api/v1/attribute")
    @ApiMessage("Tạo mới thuộc tính")
    public ResponseEntity<IdResponse> createAttribute(
            @Valid
            @RequestBody AttributeDTO dto
    ) throws NameExisted {
        boolean nameExists = this.attributeService.nameExists(dto.getName());
        if (nameExists) {
            throw new NameExisted("Name " +
                    dto.getName() + " đã tồn tại");
        }
        long newId = attributeService.handleCreateAttribute(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }
    @GetMapping(value = "/api/v1/attribute", params = "id")
    @ApiMessage("Lấy thông tin thuộc tính theo id")
    public ResponseEntity<AttributeDTO> getDetailsAttribute(
            @RequestParam Long id
    ) {
        AttributeDTO dto = attributeService.handleGetAttributeById(id);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping(value = "/api/v1/attribute", params = "id")
    @ApiMessage("Xóa thuộc tính")
    public ResponseEntity<String> deleteAttribute(
            @RequestParam Long id
    ) {
        boolean isValidId = attributeService.existsById(id);
        if (!isValidId) {
            throw new IdInvalidException("ID " + id + " không có");
        }
        this.attributeService.handleDeleteAttribute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xoá thành công");
    }
}
