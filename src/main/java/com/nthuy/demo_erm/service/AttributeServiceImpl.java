package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.constant.EnumAttributeDisplayType;
import com.nthuy.demo_erm.dto.AttributeDTO;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.AttributeValueDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.AttributeValueEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.mapper.AttributeMapper;
import com.nthuy.demo_erm.mapper.AttributeValueMapper;
import com.nthuy.demo_erm.repository.AttributeRepository;
import com.nthuy.demo_erm.repository.AttributeValueRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService{

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;

    public AttributeServiceImpl(AttributeRepository attributeRepository, AttributeValueRepository attributeValueRepository, AttributeMapper attributeMapper, AttributeValueMapper attributeValueMapper) {
        this.attributeRepository = attributeRepository;
        this.attributeValueRepository = attributeValueRepository;
        this.attributeMapper = attributeMapper;
        this.attributeValueMapper = attributeValueMapper;
    }


    @Override
    public boolean nameExists(String name) {
        return this.attributeRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return this.attributeRepository.existsById(id);
    }

    @Transactional
    public Long handleCreateAttribute(AttributeDTO dto) {
        // gọi validate riêng
        validateAttribute(dto);

        EnumAttributeDisplayType displayType = dto.getDisplayType() == null
                ? EnumAttributeDisplayType.TEXTBOX
                : dto.getDisplayType();

        if (!EnumAttributeDisplayType.TEXTBOX.equals(displayType)) {
            dto.setDataType(null);
        }

        AttributeEntity attribute = attributeMapper.toEntity(dto);
        attribute.setDisplayType(displayType);
        attribute.setDataType(dto.getDataType());
        attribute = attributeRepository.save(attribute);

        if (!EnumAttributeDisplayType.TEXTBOX.equals(displayType) &&
                dto.getValues() != null && !dto.getValues().isEmpty()) {

            for (AttributeValueDTO v : dto.getValues()) {
                AttributeValueEntity av = attributeValueMapper.toEntity(v);
                av.setValue(v.getValue().trim());
                av.setAttributeId(attribute.getId());
                attributeValueRepository.save(av);
            }
        }

        return attribute.getId();
    }
    @Override
    public AttributeDTO handleGetAttributeById(Long id) {

        AttributeEntity entity = this.attributeRepository.findById(id)
                .orElseThrow(() -> new BadRequestValidationException("Thuộc tính với ID " + id + " không tồn tại"));
       AttributeDTO dto = attributeMapper.toDto(entity);
        if (!dto.getDisplayType().equals(EnumAttributeDisplayType.TEXTBOX)) {
           List<AttributeValueDTO> values = attributeValueRepository.findByAttributeId(id)
                   .stream().map(attributeValueMapper::toDto).collect(Collectors.toList());
           dto.setValues(values);
       }
        return dto;
    }

    @Override
    public void handleDeleteAttribute(Long id) {
        this.attributeRepository.deleteById(id);
    }

    @Override
    public Long handleUpdateAttribute(AttributeDTO dto) {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<AttributeDTO> handleGetAttribute(String code, String name, Boolean isActive, Pageable pageable) {
        return null;
    }
    public void validateAttribute(AttributeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload không được null");
        }

        EnumAttributeDisplayType displayType = dto.getDisplayType() == null
                ? EnumAttributeDisplayType.TEXTBOX
                : dto.getDisplayType();

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
