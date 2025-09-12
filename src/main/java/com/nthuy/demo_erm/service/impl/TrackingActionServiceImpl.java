package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.dto.DepartmentDTO;
import com.nthuy.demo_erm.dto.TrackingActionDTO;
import com.nthuy.demo_erm.entity.RiskTrackingActionEntity;
import com.nthuy.demo_erm.entity.TrackingActionEntity;
import com.nthuy.demo_erm.entity.TrackingActionMapEntity;
import com.nthuy.demo_erm.mapper.TrackingActionMapper;
import com.nthuy.demo_erm.repository.RiskTrackingActionRepository;
import com.nthuy.demo_erm.repository.RiskTrackingReasonRepository;
import com.nthuy.demo_erm.repository.TrackingActionMapRepository;
import com.nthuy.demo_erm.repository.TrackingActionRepository;
import com.nthuy.demo_erm.service.TrackingActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TrackingActionServiceImpl implements TrackingActionService {
    private final TrackingActionRepository trackingActionRepository;
    private final TrackingActionMapper trackingActionMapper;
    private final RiskTrackingActionRepository riskTrackingActionRepository;

    private final TrackingActionMapRepository trackingActionMapRepository;

    @Transactional
    public void creates(List<TrackingActionDTO> dtos) {
        List<TrackingActionEntity> entities = trackingActionMapper.toEntityList(dtos);
        trackingActionRepository.saveAll(entities);
    }
//    @Transactional
//    public void create(List<TrackingActionDTO> dtos, Long riskTrackingReasonId) {
//        List<TrackingActionEntity> entities = trackingActionMapper.toEntityList(dtos);
//        for (TrackingActionEntity entity : entities) {
//            trackingActionRepository.save(entity);
//
//            RiskTrackingActionEntity mapping = new RiskTrackingActionEntity();
//            mapping.setRiskTrackingReasonId(riskTrackingReasonId);
//            mapping.setTrackingActionId(entity.getId());
//            riskTrackingActionRepository.save(mapping);
//        }
//    }
@Transactional
public void create(List<TrackingActionDTO> dtos, Long riskTrackingReasonId) {
    for (TrackingActionDTO dto : dtos) {
        // Chuyển DTO sang Entity và lưu
        TrackingActionEntity entity = trackingActionMapper.toEntity(dto);
        trackingActionRepository.saveAndFlush(entity); // flush để có ID

        // Lưu mapping RiskTrackingAction
        RiskTrackingActionEntity riskMapping = new RiskTrackingActionEntity();
        riskMapping.setRiskTrackingReasonId(riskTrackingReasonId);
        riskMapping.setTrackingActionId(entity.getId());
        riskTrackingActionRepository.save(riskMapping);

        // Lưu mapping TrackingAction -> Department
        Set<DepartmentDTO> departments = Optional.ofNullable(dto.getDepartments())
                .orElse(Collections.emptySet());

        for (DepartmentDTO dep : departments) {
            TrackingActionMapEntity map = new TrackingActionMapEntity();
            map.setTrackingActionId(entity.getId());
            map.setDepartmentId(dep.getId());
            trackingActionMapRepository.save(map);
        }
    }
}

}
