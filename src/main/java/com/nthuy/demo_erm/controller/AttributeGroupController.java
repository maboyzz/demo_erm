package com.nthuy.demo_erm.controller;

import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.IdResponse;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.exception.TypeAttributeGroupValidException;
import com.nthuy.demo_erm.service.AttributeGroupService;
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
    @GetMapping(value = "/api/v1/attribute-group", params = "id")
    @ApiMessage("Lấy thông tin nhóm thuộc tính theo id")
    public ResponseEntity<AttributeGroupDTO> getDetailsAttributeGroup(
            @RequestParam Long id
    ) {
        AttributeGroupDTO dto = attributeGroupService.handleGetAttributeGroupById(id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping(value = "/api/v1/attribute-group", params = "id")
    @ApiMessage("Xóa nhóm thuộc tính")
    public ResponseEntity<String> deleteAttributeGroup(
            @RequestParam Long id
    ) {
        boolean isValidId = attributeGroupService.existsById(id);
        if (!isValidId) {
            throw new IdInvalidException("ID " + id + " không có");
        }
        this.attributeGroupService.handleDeleteAttributeGroup(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xoá thành công");
    }

    @PutMapping("/api/v1/attribute-group")
    @ApiMessage("Cập Nhật nhóm thuộc tính")
    public ResponseEntity<IdResponse> updateAttributeGroup(
            @Valid
            @RequestBody AttributeGroupDTO dto
    ) throws NameExisted {
        boolean nameExists = this.attributeGroupService.nameExists(dto.getName());
        if (nameExists) {
            throw new NameExisted("Username " +
                    dto.getName() + " đã tồn tại");
        }
        long newId = attributeGroupService.handleUpdateAttributeGroup(dto);
        IdResponse idResponse = new IdResponse(newId);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }
    @GetMapping("/api/v1/attribute-group")
    public ResponseEntity<ResultPaginationDTO<AttributeGroupDTO>> getAttributeGroups(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        // Tạo Pageable từ param sort (vd: id,desc)
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        ResultPaginationDTO<AttributeGroupDTO> result =
                attributeGroupService.handleGetAttributeGroup(code, name, isActive, pageable);
        return ResponseEntity.ok(result);
    }

}
