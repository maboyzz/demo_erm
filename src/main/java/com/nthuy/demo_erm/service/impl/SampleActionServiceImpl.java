package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.constant.EnumAttributeDisplayType;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.common.until.SpecificationUtils;
import com.nthuy.demo_erm.config.AttributeSpecification;
import com.nthuy.demo_erm.config.SampleActionSpecification;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.dto.response.*;
import com.nthuy.demo_erm.entity.*;
import com.nthuy.demo_erm.mapper.SampleActionLineMapper;
import com.nthuy.demo_erm.mapper.SampleActionMapper;
import com.nthuy.demo_erm.proxy.DepartmentProxy;
import com.nthuy.demo_erm.repository.*;
import com.nthuy.demo_erm.service.SampleActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SampleActionServiceImpl implements SampleActionService {
    private final SampleActionLineRepository sampleActionLineRepository;
    private final SampleActionRepository sampleActionRepository;
    private final SampleActionLineMapRepository sampleActionLineMapRepository;
    private final ClassifyReasonRepository classifyReasonRepository;
    private final RiskTypeRepository riskTypeRepository;
    private final SampleActionMapper sampleActionMapper;
    private final SampleActionLineMapper sampleActionLineMapper;
    private final HandlingMeasureRepository handlingMeasureRepository;

    private final DepartmentProxy departmentProxy;


    @Override
    @Transactional
    public Long create(SampleActionDTO dto) throws NameExisted {
        // Validate code & name
        validateCodeNotExists(dto.getCode(), null);
        validateNameNotExists(dto.getName(), null);

        // Map DTO -> Entity
        SampleActionEntity entity = sampleActionMapper.toEntity(dto);
        sampleActionRepository.save(entity);

        // Xử lý sample action lines
        if (dto.getSampleActionLines() != null && !dto.getSampleActionLines().isEmpty()) {
            for (SampleActionLineDTO splDto : dto.getSampleActionLines()) {
                SampleActionLineEntity splEntity = sampleActionLineMapper.toEntity(splDto);
                splEntity.setSampleActionId(entity.getId());

                // ✅ Lưu trước để lấy id
                sampleActionLineRepository.save(splEntity);

                // Lấy danh sách departmentIds
                Set<Long> departmentIds = (splDto.getDepartments() != null && !splDto.getDepartments().isEmpty()) ? splDto.getDepartments().stream().map(DepartmentDTO::getId).collect(Collectors.toSet()) : Collections.emptySet();

                // ✅ Bây giờ mới map, vì splEntity đã có id
                saveSampleActionLineSystemMap(splEntity.getId(), departmentIds);
            }
        }

        return entity.getId();
    }

    @Override
    public SampleActionDTO getSampleAction(Long id) {
        validateIdExists(id);
        // Lấy entity từ repository, nếu không tồn tại thì ném ngoại lệ
        SampleActionEntity entity = sampleActionRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));

        // Ánh xạ entity -> DTO
        SampleActionDTO dto = sampleActionMapper.toDto(entity);

        // Ánh xạ classifyReason nếu có
        Optional.ofNullable(entity.getClassifyReasonId()).flatMap(classifyReasonRepository::findById).ifPresent(classify -> dto.setClassifyReason(new ClassifyReasonResponse(classify.getId(), classify.getCode(), classify.getName())));

        // Ánh xạ riskType nếu có
        Optional.ofNullable(entity.getRiskTypeId()).flatMap(riskTypeRepository::findById).ifPresent(riskType -> dto.setRiskType(new RiskTypeResponse(riskType.getId(), riskType.getCode(), riskType.getName())));

        // Lấy danh sách SampleActionLine và ánh xạ sang DTO
        List<SampleActionLineEntity> lineEntities = sampleActionLineRepository.findBySampleActionId(id);
        List<SampleActionLineDTO> lineDtos = new ArrayList<>();

        for (SampleActionLineEntity lineEntity : lineEntities) {

            SampleActionLineDTO lineDto = sampleActionLineMapper.toDto(lineEntity);

            // Dùng ID từ entity để đảm bảo đúng dữ liệu
            Set<DepartmentDTO> departments = getSystemsBySampleActionLineId(lineEntity.getId());
            lineDto.setDepartments(departments);
            Optional.ofNullable(lineEntity.getHandlingMeasureId()).flatMap(handlingMeasureRepository::findById).ifPresent(handlingMeasure -> lineDto.setHandlingMeasure(new HandlingMeasureResponse(handlingMeasure.getId(), handlingMeasure.getCode(), handlingMeasure.getName())));

            lineDtos.add(lineDto);
        }

        dto.setSampleActionLines(lineDtos);
        return dto;
    }


    @Override
    public void delete(Long id) {
        validateIdExists(id);
        sampleActionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(SampleActionDTO dto) throws NameExisted {
        // Validate code & name (không được trùng với record khác)
        validateCodeNotExists(dto.getCode(), dto.getId());
        validateNameNotExists(dto.getName(), dto.getId());

        // Lấy entity hiện có
        SampleActionEntity entity = sampleActionRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("SampleAction not found with id: " + dto.getId()));

        // Map DTO -> Entity (cập nhật field)
        sampleActionMapper.updateEntityFromDto(dto, entity);

        // Lưu lại entity chính
        sampleActionRepository.save(entity);

        // Xử lý sample action lines
        sampleActionLineRepository.deleteBySampleActionId(entity.getId());

        if (dto.getSampleActionLines() != null && !dto.getSampleActionLines().isEmpty()) {
            for (SampleActionLineDTO splDto : dto.getSampleActionLines()) {
                SampleActionLineEntity splEntity = sampleActionLineMapper.toEntity(splDto);
                splEntity.setSampleActionId(entity.getId());

                // Lưu line
                splEntity = sampleActionLineRepository.saveAndFlush(splEntity);

                // Map departments
                Set<Long> departmentIds = (splDto.getDepartments() != null && !splDto.getDepartments().isEmpty()) ? splDto.getDepartments().stream().map(DepartmentDTO::getId).collect(Collectors.toSet()) : Collections.emptySet();

                saveSampleActionLineSystemMap(splEntity.getId(), departmentIds);
            }
        }

        return entity.getId();
    }

    @Override
    public ResultPaginationDTO<SampleActionDTO> getListSampleAction(String code, String name, Long riskTypeId, Long classifyReasonId, Boolean isActive, Pageable pageable) {
        Specification<SampleActionEntity> spec = Specification.where(null);

        spec = SpecificationUtils.addIfHasText(spec, code, SampleActionSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, name, SampleActionSpecification::hasName);
        spec = SpecificationUtils.addIfNotNull(spec, isActive, SampleActionSpecification::hasIsActive);
        spec = SpecificationUtils.addIfNotNull(spec, riskTypeId, SampleActionSpecification::hasRiskType);
        spec = SpecificationUtils.addIfNotNull(spec, classifyReasonId, SampleActionSpecification::hasClassifyReason);

        Page<SampleActionEntity> pageResult = sampleActionRepository.findAll(spec, pageable);

        if (pageResult.isEmpty()) {
            return PaginationUtils.buildResult(pageResult, Collections.emptyList(), pageable);
        }
        List<SampleActionDTO> dtoList = pageResult.getContent().stream().map(entity -> {
            SampleActionDTO dto = sampleActionMapper.toDto(entity);

            // load attribute group
            Optional.ofNullable(entity.getRiskTypeId()).flatMap(riskTypeRepository::findById).ifPresent(riskType -> dto.setRiskType(new RiskTypeResponse(riskType.getId(), riskType.getCode(), riskType.getName())));
            Optional.ofNullable(entity.getClassifyReasonId()).flatMap(classifyReasonRepository::findById).ifPresent(classifyReason -> dto.setClassifyReason(new ClassifyReasonResponse(classifyReason.getId(), classifyReason.getCode(), classifyReason.getName())));

            return dto;
        }).toList();

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }


    // ---------------- HELPER METHODS ----------------
    private void saveSampleActionLineSystemMap(Long reasonId, Set<Long> departmentIds) {
        List<SampleActionLineMapEntity> mapEntities = departmentIds.stream().map(departmentId -> {
            SampleActionLineMapEntity mapEntity = new SampleActionLineMapEntity();
            mapEntity.setSampleActionLineId(reasonId);
            mapEntity.setDepartmentId(departmentId);
            return mapEntity;
        }).collect(Collectors.toList());

        sampleActionLineMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<DepartmentDTO> getSystemsBySampleActionLineId(Long sampleActionLineId) {
        List<SampleActionLineMapEntity> mapEntities = sampleActionLineMapRepository.findBySampleActionLineId(sampleActionLineId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> departmentIds = mapEntities.stream().map(SampleActionLineMapEntity::getDepartmentId).collect(Collectors.toSet());
        Map<Long, DepartmentDTO> departmentDTOMap = departmentProxy.getDepartments(departmentIds);
        return departmentIds.stream().map(departmentDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<DepartmentDTO> getSystemsBySampleActionLineIdOptimized(Long reasonId, List<SampleActionLineMapEntity> allMappings, Map<Long, DepartmentDTO> departmentDTOMap) {
        Set<Long> departmentIds = allMappings.stream().filter(mapping -> mapping.getSampleActionLineId().equals(reasonId)).map(SampleActionLineMapEntity::getDepartmentId).collect(Collectors.toSet());

        return departmentIds.stream().map(departmentDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = sampleActionRepository.existsByName(name);
        } else {
            exists = sampleActionRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = sampleActionRepository.existsByCode(code);
        } else {
            exists = sampleActionRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!sampleActionRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }


}
