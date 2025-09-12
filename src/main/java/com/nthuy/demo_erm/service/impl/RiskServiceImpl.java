package com.nthuy.demo_erm.service.impl;


import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.common.exception.BadRequestValidationException;
import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.dto.response.*;
import com.nthuy.demo_erm.entity.*;

import com.nthuy.demo_erm.mapper.*;
import com.nthuy.demo_erm.proxy.DepartmentProxy;
import com.nthuy.demo_erm.proxy.EmployeeProxy;
import com.nthuy.demo_erm.proxy.SystemProxy;
import com.nthuy.demo_erm.repository.*;
import com.nthuy.demo_erm.service.RiskService;
import com.nthuy.demo_erm.service.TagService;
import com.nthuy.demo_erm.service.TrackingActionService;
import com.nthuy.demo_erm.service.TrackingReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RiskServiceImpl implements RiskService {
    private final RiskRepository riskRepository;
    private final RiskLineRepository riskLineRepository;
    private final RiskLineValueRepository riskLineValueRepository;
    private final EmployeeProxy employeeProxy;
    private final SystemProxy systemProxy;
    private final DepartmentProxy departmentProxy;
    private final RiskMapper riskMapper;
    private final RiskLineMapper riskLineMapper;
    private final RiskLineValueMapper riskLineValueMapper;
    private final RiskTypeRepository riskTypeRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final TagService tagService;
    private final RiskTagRepository riskTagRepository;
    private final RiskFileRepository riskFileRepository;
    private final RiskFileMapper riskFileMapper;
    private final TrackingReasonService trackingReasonService;
    private final RiskTrackingReasonRepository riskTrackingReasonRepository;
    private final TrackingReasonMapper trackingReasonMapper;
    private final TrackingReasonRepository trackingReasonRepository;
    private final TrackingActionService trackingActionService;
    private final TrackingActionRepository trackingActionRepository;
    private final TrackingActionMapper trackingActionMapper;
    private final RiskTrackingReasonMapper riskTrackingReasonMapper;
    private final HandlingMeasureRepository handlingMeasureRepository;
    private final TrackingActionMapRepository trackingActionMapRepository;
    Pageable pageable = PageRequest.of(0, 1000);

    @Override
    @Transactional
    public Long create(RiskDTO dto) throws NameExisted {
        validateCodeNotExists(dto.getCode(), null);
        validateNameNotExists(dto.getName(), null);

        RiskEntity entity = riskMapper.toEntity(dto);

        riskRepository.save(entity);

        Set<Long> tagIds = (dto.getTags() != null && !dto.getTags().isEmpty()) ? dto.getTags().stream().map(TagDTO::getId).collect(Collectors.toSet()) : Collections.emptySet();
        saveRiskTag(entity.getId(), tagIds);

        for (RiskLineDTO riskLineDto : dto.getRiskLine()) {
            RiskLineEntity riskLineEntity = riskLineMapper.toEntity(riskLineDto);
            riskLineEntity.setRiskId(entity.getId());
            // ✅ Lưu trước để lấy id
            riskLineRepository.save(riskLineEntity);
            if (riskLineDto.getLineValues() != null) {
                for (RiskLineValueDTO rlvDto : riskLineDto.getLineValues()) {
                    RiskLineValueEntity rlvEntity = riskLineValueMapper.toEntity(rlvDto);
                    rlvEntity.setRiskLineId(riskLineEntity.getId());
                    riskLineValueRepository.save(rlvEntity);
                }
            }
        }
        if (dto.getRiskFile() != null) {
            for (RiskFileDTO riskFileDto : dto.getRiskFile()) {
                RiskFileEntity rfEntity = riskFileMapper.toEntity(riskFileDto);
                rfEntity.setRiskId(entity.getId());
                riskFileRepository.save(rfEntity);
            }
        }
        if (dto.getTrackingReason() != null && !dto.getTrackingReason().isEmpty()) {
            for (TrackingReasonDTO tr : dto.getTrackingReason()) {
                Long trackingReasonId;

                if (tr.getId() == null) {
                    TrackingReasonEntity reasonEntity = trackingReasonMapper.toEntity(tr);
                    reasonEntity.setCount(1);
                    trackingReasonRepository.save(reasonEntity);
                    trackingReasonId = reasonEntity.getId();
                    tr.setId(trackingReasonId);
                } else {
                    TrackingReasonEntity existingEntity = trackingReasonRepository.findById(tr.getId()).orElseThrow(() -> new BadRequestValidationException("TrackingReason ID " + tr.getId() + " không tồn tại"));

                    int currentCount = existingEntity.getCount();
                    existingEntity.setCount(currentCount + 1);
                    trackingReasonRepository.save(existingEntity);

                    trackingReasonId = existingEntity.getId();
                }

                RiskTrackingReasonEntity mappingEntity = new RiskTrackingReasonEntity();
                mappingEntity.setRiskId(entity.getId());
                mappingEntity.setTrackingReasonId(trackingReasonId);
                riskTrackingReasonRepository.save(mappingEntity);

                Long riskTrackingReasonId = mappingEntity.getId();

                if (tr.getTrackingActions() != null && !tr.getTrackingActions().isEmpty()) {
                    trackingActionService.create(tr.getTrackingActions(), riskTrackingReasonId);

                }
            }
        }
        return entity.getId();
    }

    @Override
    public RiskDTO getRisk(Long id) {
        validateIdExists(id);
        RiskEntity entity = riskRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));
        RiskDTO dto = riskMapper.toDto(entity);

        // Ánh xạ riskType nếu có
        Optional.ofNullable(entity.getRiskTypeId()).flatMap(riskTypeRepository::findById).ifPresent(riskType -> dto.setRiskType(new RiskTypeResponse(riskType.getId(), riskType.getCode(), riskType.getName())));

        // Ánh xạ riskCategory nếu có
        Optional.ofNullable(entity.getRiskCategoryId()).flatMap(riskCategoryRepository::findById).ifPresent(riskCategory -> dto.setRiskCategory(new RiskCategoryResponse(riskCategory.getId(), riskCategory.getCode(), riskCategory.getName())));

        getSystemByRiskId(id).ifPresent(dto::setSystem);

        dto.setTags(getTagsByRiskId(dto.getId()));

        dto.setTrackingReason(getTrackingReasonByRiskId(dto.getId()));


        //
        Set<TrackingReasonDTO> trackingReasons = getTrackingReasonByRiskId(dto.getId());

        for (TrackingReasonDTO trackingReasonDTO : trackingReasons) {
            List<TrackingActionEntity> actionEntities = trackingActionRepository.findByRiskIdAndTrackingReasonId(dto.getId(), trackingReasonDTO.getId());

            List<TrackingActionDTO> actionDTOs = new ArrayList<>();

            for (TrackingActionEntity actionEntity : actionEntities) {
                TrackingActionDTO actionDTO = trackingActionMapper.toDto(actionEntity);

                // ✅ set handlingMeasure nếu có
                Optional.ofNullable(actionEntity.getHandlingMeasureId()).flatMap(handlingMeasureRepository::findById).ifPresent(handlingMeasure -> actionDTO.setHandlingMeasure(new HandlingMeasureResponse(handlingMeasure.getId(), handlingMeasure.getCode(), handlingMeasure.getName())));
                Set<DepartmentDTO> departments = getDepartmentByTrackingActionId(actionEntity.getId());
                actionDTO.setDepartments(departments);
                actionDTOs.add(actionDTO);
            }

            // ✅ luôn set, nếu không có thì là list rỗng chứ không phải null
            trackingReasonDTO.setTrackingActions(actionDTOs);
        }

        dto.setTrackingReason(trackingReasons);


        List<RiskLineEntity> lineEntities = riskLineRepository.findByRiskId(id);
        List<RiskLineDTO> lineDTOs = new ArrayList<>();

        for (RiskLineEntity lineEntity : lineEntities) {
            RiskLineDTO lineDTO = riskLineMapper.toDto(lineEntity);

            Optional.ofNullable(lineEntity.getAttributeId()).flatMap(attributeRepository::findById).ifPresent(attribute -> lineDTO.setAttribute(AttributeResponse.builder().id(attribute.getId()).code(attribute.getCode()).name(attribute.getName()).displayType(attribute.getDisplayType()).dataType(attribute.getDataType()).description(attribute.getDescription()).active(attribute.isActive()).build()));
            Optional.ofNullable(lineEntity.getAttributeGroupId()).flatMap(attributeGroupRepository::findById).ifPresent(attributeGroup -> lineDTO.setAttributeGroup(new AttributeGroupResponse(attributeGroup.getId(), attributeGroup.getCode(), attributeGroup.getName())));

            List<RiskLineValueEntity> lineValueEntities = riskLineValueRepository.findByRiskLineId(lineEntity.getId());
            List<RiskLineValueDTO> lineValueDTOs = new ArrayList<>();
            for (RiskLineValueEntity lineValueEntity : lineValueEntities) {
                RiskLineValueDTO lineValueDTO = riskLineValueMapper.toDto(lineValueEntity);
                Optional.ofNullable(lineValueEntity.getAttributeValueId()).flatMap(attributeValueRepository::findById).ifPresent(attributeValue -> lineValueDTO.setAttributeValue(new AttributeValueDTO(attributeValue.getId(), attributeValue.getValue(), attributeValue.getAttributeId())));
                lineValueDTOs.add(lineValueDTO);
            }
            lineDTO.setLineValues(lineValueDTOs);
            lineDTOs.add(lineDTO);
        }

        List<RiskFileEntity> fileEntities = riskFileRepository.findByRiskId(id);
        List<RiskFileDTO> fileDTOs = new ArrayList<>();
        for (RiskFileEntity fileEntity : fileEntities) {
            RiskFileDTO fileDTO = riskFileMapper.toDto(fileEntity);
            fileDTOs.add(fileDTO);
        }
        dto.setRiskFile(fileDTOs);
        dto.setRiskLine(lineDTOs);


        return dto;
    }


    @Override
    public void delete(Long id) {

    }

    @Override
    public Long update(RiskDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<RiskDTO> getListRisk(String code, String name, List<Long> systemIds, Boolean isActive, EnumTypeReason type, Pageable pageable) {
        return null;
    }

    // ----------------- HELPER -----------------

    private Optional<EmployeeDTO> getEmployeeByRiskId(Long riskId) {
        return riskRepository.findById(riskId).map(risk -> {
            Long reporterId = risk.getReporterId();
            if (reporterId == null) {
                return null;
            }
            return employeeProxy.getEmployee(reporterId);
        });
    }

    private Optional<SystemDTO> getSystemByRiskId(Long riskId) {
        return riskRepository.findById(riskId).map(risk -> {
            Long systemId = risk.getSystemId();
            if (systemId == null) {
                return null;
            }
            return systemProxy.getSystem(systemId);
        });
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<TagDTO> getTagsByRiskId(Long riskId) {
        List<RiskTagEntity> mapEntities = riskTagRepository.findByRiskId(riskId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> tagIds = mapEntities.stream().map(RiskTagEntity::getTagId).collect(Collectors.toSet());

        ResultPaginationDTO<TagDTO> result = tagService.getListTag(tagIds, pageable);

        if (result == null || result.getContent() == null) {
            return Collections.emptySet();
        }
        Map<Long, TagDTO> tagDTOMap = result.getContent().stream().collect(Collectors.toMap(TagDTO::getId, Function.identity()));

        return tagIds.stream().map(tagDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private Set<TrackingReasonDTO> getTrackingReasonByRiskId(Long riskId) {
        List<RiskTrackingReasonEntity> mapEntities = riskTrackingReasonRepository.findByRiskId(riskId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> trackingIds = mapEntities.stream().map(RiskTrackingReasonEntity::getTrackingReasonId).collect(Collectors.toSet());

        ResultPaginationDTO<TrackingReasonDTO> result = trackingReasonService.getListTrackingReason(trackingIds, pageable);

        if (result == null || result.getContent() == null) {
            return Collections.emptySet();
        }
        Map<Long, TrackingReasonDTO> TrackingReasonDTOMap = result.getContent().stream().collect(Collectors.toMap(TrackingReasonDTO::getId, Function.identity()));

        return trackingIds.stream().map(TrackingReasonDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }


    private Set<RiskTrackingReasonDTO> getRiskTrackingReasonByRiskId(Long riskId) {
        List<RiskTrackingReasonEntity> entities = riskTrackingReasonRepository.findByRiskId(riskId);

        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }

        return entities.stream().map(riskTrackingReasonMapper::toDto)  // convert sang DTO
                .collect(Collectors.toSet());
    }

    private Map<Long, TagDTO> getTags(Set<Long> tagIds, Pageable pageable) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyMap();
        }

        ResultPaginationDTO<TagDTO> result = tagService.getListTag(tagIds, pageable);

        if (result != null && result.getContent() != null) {
            return result.getContent().stream().collect(Collectors.toMap(TagDTO::getId, Function.identity()));
        }

        return Collections.emptyMap();
    }

    private void saveRiskTag(Long riskId, Set<Long> tagIds) {
        List<RiskTagEntity> mapEntities = tagIds.stream().map(tagId -> {
            RiskTagEntity mapEntity = new RiskTagEntity();
            mapEntity.setRiskId(riskId);
            mapEntity.setTagId(tagId);
            return mapEntity;
        }).collect(Collectors.toList());

        riskTagRepository.saveAll(mapEntities);
    }

    private void saveRiskTrackingReason(Long riskId, Set<Long> trackingIds) {
        List<RiskTrackingReasonEntity> mapEntities = trackingIds.stream().map(tagId -> {
            RiskTrackingReasonEntity mapEntity = new RiskTrackingReasonEntity();
            mapEntity.setRiskId(riskId);
            mapEntity.setTrackingReasonId(tagId);
            return mapEntity;
        }).collect(Collectors.toList());

        riskTrackingReasonRepository.saveAll(mapEntities);
    }


    // ---------------- HELPER METHODS ----------------
    private void saveTrackingActionDepartmentMap(Long trackingActionId, Set<Long> departmentIds) {
        List<TrackingActionMapEntity> mapEntities = departmentIds.stream().map(departmentId -> {
            TrackingActionMapEntity mapEntity = new TrackingActionMapEntity();
            mapEntity.setTrackingActionId(trackingActionId);
            mapEntity.setDepartmentId(departmentId);
            return mapEntity;
        }).collect(Collectors.toList());

        trackingActionMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<DepartmentDTO> getDepartmentByTrackingActionId(Long trackingActionId) {
        List<TrackingActionMapEntity> mapEntities = trackingActionMapRepository.findByTrackingActionId(trackingActionId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> departmentIds = mapEntities.stream().map(TrackingActionMapEntity::getDepartmentId).collect(Collectors.toSet());
        Map<Long, DepartmentDTO> departmentDTOMap = departmentProxy.getDepartments(departmentIds);
        return departmentIds.stream().map(departmentDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskRepository.existsByName(name);
        } else {
            exists = riskRepository.existsByNameAndIdNot(name, excludeId);
        }
        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    private void validateCodeNotExists(String code, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = riskRepository.existsByCode(code);
        } else {
            exists = riskRepository.existsByCodeAndIdNot(code, excludeId);
        }
        if (exists) {
            throw new NameExisted("Code đã tồn tại: " + code);
        }
    }

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!riskRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }


}
