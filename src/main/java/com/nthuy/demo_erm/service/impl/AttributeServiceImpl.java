package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.constant.EnumAttributeDisplayType;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.AttributeSpecification;
import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.dto.AttributeValueDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.response.AttributeGroupResponse;
import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.AttributeValueEntity;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.mapper.AttributeMapper;
import com.nthuy.demo_erm.mapper.AttributeValueMapper;
import com.nthuy.demo_erm.repository.AttributeGroupRepository;
import com.nthuy.demo_erm.repository.AttributeRepository;
import com.nthuy.demo_erm.repository.AttributeValueRepository;
import com.nthuy.demo_erm.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;
    private final AttributeGroupRepository attributeGroupRepository;

    @Override
    @Transactional
    public Long create(AttributeDTO dto) throws NameExisted {

        validateNameNotExists(dto.getName(), null);
        validateCodeNotExists(dto.getCode(), null);
        validateAttribute(dto);

        EnumAttributeDisplayType displayType = getDisplayTypeOrDefault(dto.getDisplayType());

        AttributeEntity attribute = attributeMapper.toEntity(dto);
        attribute.setDisplayType(displayType);

        // TEXTBOX giữ dataType, các loại khác bỏ dataType
        if (EnumAttributeDisplayType.TEXTBOX.equals(displayType)) {
            attribute.setDataType(dto.getDataType());
        } else {
            attribute.setDataType(null);
        }

        attributeRepository.save(attribute);

        // Nếu là SELECT hoặc kiểu khác TEXTBOX thì lưu values
        if (!EnumAttributeDisplayType.TEXTBOX.equals(displayType) && dto.getValues() != null && !dto.getValues().isEmpty()) {

            dto.getValues().stream().map(v -> {
                AttributeValueEntity av = attributeValueMapper.toEntity(v);
                av.setValue(v.getValue().trim());
                av.setAttributeId(attribute.getId());
                return av;
            }).forEach(attributeValueRepository::save);
        }

        return attribute.getId();
    }

    @Override
    public AttributeDTO getAttribute(Long id) {
        AttributeEntity entity = attributeRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("Thuộc tính với ID " + id + " không tồn tại"));

        AttributeDTO dto = attributeMapper.toDto(entity);

        // load attribute group (nếu có)
        Optional.ofNullable(entity.getAttributeGroupId()).flatMap(attributeGroupRepository::findById).ifPresent(group -> dto.setAttributeGroup(new AttributeGroupResponse(group.getId(), group.getCode(), group.getName())));

        // load values (nếu không phải TEXTBOX)
        if (dto.getDisplayType() != EnumAttributeDisplayType.TEXTBOX) {
            List<AttributeValueDTO> values = attributeValueRepository.findByAttributeId(id).stream().map(attributeValueMapper::toDto).toList();
            dto.setValues(values);
        }
        return dto;
    }

    @Override
    public void delete(Long id) {
        validateIdExists(id);
        attributeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(AttributeDTO dto) throws NameExisted {
        AttributeEntity entity = attributeRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Thuộc tính với ID " + dto.getId() + " không tồn tại"));

        validateNameNotExists(dto.getName(), dto.getId());
        validateCodeNotExists(dto.getCode(), dto.getId());
        validateAttribute(dto);

        // update entity từ DTO
        attributeMapper.updateEntityFromDto(dto, entity);

        EnumAttributeDisplayType newDisplayType = getDisplayTypeOrDefault(dto.getDisplayType());
        entity.setDisplayType(newDisplayType);

        if (EnumAttributeDisplayType.TEXTBOX.equals(newDisplayType)) {
            // --- TEXTBOX: giữ dataType, xóa values ---
            entity.setDataType(dto.getDataType());
            attributeValueRepository.deleteByAttributeId(entity.getId());

        } else {
            // --- SELECTBOX hoặc loại khác ---
            entity.setDataType(null);

            // Xóa values cũ trước
            attributeValueRepository.deleteByAttributeId(entity.getId());

            // Thêm values mới nếu DTO có
            if (dto.getValues() != null && !dto.getValues().isEmpty()) {
                List<AttributeValueEntity> newValues = dto.getValues().stream().map(v -> {
                    AttributeValueEntity av = attributeValueMapper.toEntity(v);
                    av.setValue(v.getValue().trim());
                    av.setAttributeId(entity.getId());
                    return av;
                }).toList();

                attributeValueRepository.saveAll(newValues);
            }
        }

        return attributeRepository.save(entity).getId();
    }

    @Override
    public ResultPaginationDTO<AttributeDTO> getListAttribute(String code, String name, Boolean isActive, Long attributeGroupId, Pageable pageable) {

        Specification<AttributeEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, code, AttributeSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, name, AttributeSpecification::hasName);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, AttributeSpecification::hasIsActive);
        spec = SpecificationUtils.addIfNotNull(spec, attributeGroupId, AttributeSpecification::hasAttributeGroup);

        Page<AttributeEntity> pageResult = attributeRepository.findAll(spec, pageable);
        if (pageResult.isEmpty()) {
            return PaginationUtils.buildResult(pageResult, Collections.emptyList(), pageable);
        }
        List<AttributeDTO> dtoList = pageResult.getContent().stream().map(entity -> {
            AttributeDTO dto = attributeMapper.toDto(entity);

            // load attribute group
            Optional.ofNullable(entity.getAttributeGroupId()).flatMap(attributeGroupRepository::findById).ifPresent(group -> dto.setAttributeGroup(new AttributeGroupResponse(group.getId(), group.getCode(), group.getName())));
            // load values nếu không phải TEXTBOX
            if (dto.getDisplayType() != EnumAttributeDisplayType.TEXTBOX) {
                List<AttributeValueDTO> values = attributeValueRepository.findByAttributeId(entity.getId()).stream().map(attributeValueMapper::toDto).toList();
                dto.setValues(values);
            }
            return dto;
        }).toList();

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ----------------- HELPER -----------------
    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = attributeRepository.existsByName(name);
        } else {
            exists = attributeRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = attributeRepository.existsByCode(code);
        } else {
            exists = attributeRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!attributeRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }

    private EnumAttributeDisplayType getDisplayTypeOrDefault(EnumAttributeDisplayType type) {
        return type == null ? EnumAttributeDisplayType.TEXTBOX : type;
    }

    private void validateAttribute(AttributeDTO dto) {

        if (dto == null) throw new IllegalArgumentException("Payload không được null");
        EnumAttributeDisplayType displayType = getDisplayTypeOrDefault(dto.getDisplayType());

        if (EnumAttributeDisplayType.TEXTBOX.equals(displayType)) {
            if (dto.getDataType() == null) {
                throw new IllegalArgumentException("Khi displayType = TEXTBOX, phải cung cấp dataType");
            }
            dto.setValues(Collections.emptyList());
        } else {
            if (dto.getValues() == null || dto.getValues().isEmpty()) {
                throw new IllegalArgumentException("Khi displayType là SELECTBOX/MULTISELECT phải cung cấp values");
            }

            Set<String> seen = new HashSet<>();
            for (AttributeValueDTO v : dto.getValues()) {
                String raw = v.getValue() == null ? "" : v.getValue().trim();
                if (raw.isEmpty()) {
                    throw new IllegalArgumentException("Attribute value không được rỗng");
                }
                if (!seen.add(raw)) {
                    throw new IllegalArgumentException("Trùng value trong danh sách: " + raw);
                }
            }
        }
    }
}