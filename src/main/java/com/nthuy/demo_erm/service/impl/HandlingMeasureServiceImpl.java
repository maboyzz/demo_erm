package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.AttributeSpecification;
import com.nthuy.demo_erm.config.HandlingMeasureSpecification;
import com.nthuy.demo_erm.dto.AttributeGroupDTO;
import com.nthuy.demo_erm.dto.HandlingMeasureDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.HandlingMeasureEntity;
import com.nthuy.demo_erm.mapper.HandlingMeasureMapper;
import com.nthuy.demo_erm.repository.HandlingMeasureRepository;
import com.nthuy.demo_erm.service.HandlingMeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HandlingMeasureServiceImpl implements HandlingMeasureService {

    private final HandlingMeasureRepository handlingMeasureRepository;
    private final HandlingMeasureMapper handlingMeasureMapper;


    @Override
    public Long create(HandlingMeasureDTO dto) throws NameExisted {
        validateNameNotExists(dto.getName(), null);
        validateCodeNotExists(dto.getCode(), null);

        HandlingMeasureEntity saveEntity = handlingMeasureMapper.toEntity(dto);
        return handlingMeasureRepository.save(saveEntity).getId();
    }

    @Override
    public HandlingMeasureDTO getHandlingMeasure(Long id) {
        HandlingMeasureEntity entity = handlingMeasureRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));
        return handlingMeasureMapper.toDto(entity);
    }

    @Override
    public void delete(Long id) {
        validateIdExists(id);
        handlingMeasureRepository.deleteById(id);
    }

    @Override
    public Long update(HandlingMeasureDTO dto) throws NameExisted {
        HandlingMeasureEntity entity = handlingMeasureRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("ID " + dto.getId() + " không tồn tại"));

        validateNameNotExists(dto.getName(), dto.getId());
        validateCodeNotExists(dto.getCode(), dto.getId());

        handlingMeasureMapper.updateEntityFromDto(dto,entity);

        return handlingMeasureRepository.save(entity).getId();
    }

    @Override
    public ResultPaginationDTO<HandlingMeasureDTO> getListHandlingMeasure(String code, String name, Boolean isActive, Pageable pageable) {
        Specification<HandlingMeasureEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, code, HandlingMeasureSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, name, HandlingMeasureSpecification::hasName);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, HandlingMeasureSpecification::hasIsActive);


        Page<HandlingMeasureEntity> pageResult = handlingMeasureRepository.findAll(spec, pageable);
        List<HandlingMeasureEntity> handlingMeasureEntities = pageResult.getContent();

        if (handlingMeasureEntities.isEmpty()) {
            return PaginationUtils.buildResult(pageResult, Collections.emptyList(), pageable);
        }

        List<HandlingMeasureDTO> dtoList = handlingMeasureMapper.toDtoList(handlingMeasureEntities);

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ----------------- HELPER -----------------
    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = handlingMeasureRepository.existsByName(name);
        } else {
            exists = handlingMeasureRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = handlingMeasureRepository.existsByCode(code);
        } else {
            exists = handlingMeasureRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!handlingMeasureRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }
}
