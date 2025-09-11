package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.dto.TrackingActionDTO;
import com.nthuy.demo_erm.entity.RiskTrackingActionEntity;
import com.nthuy.demo_erm.entity.TrackingActionEntity;
import com.nthuy.demo_erm.mapper.TrackingActionMapper;
import com.nthuy.demo_erm.repository.RiskTrackingActionRepository;
import com.nthuy.demo_erm.repository.RiskTrackingReasonRepository;
import com.nthuy.demo_erm.repository.TrackingActionRepository;
import com.nthuy.demo_erm.service.TrackingActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TrackingActionServiceImpl implements TrackingActionService {
    private final TrackingActionRepository trackingActionRepository;
    private final TrackingActionMapper trackingActionMapper;
    private final RiskTrackingActionRepository riskTrackingActionRepository;

    @Transactional
    public void creates(List<TrackingActionDTO> dtos) {
        List<TrackingActionEntity> entities = trackingActionMapper.toEntityList(dtos);
        trackingActionRepository.saveAll(entities);
    }
    @Transactional
    public void create(List<TrackingActionDTO> dtos, Long riskTrackingReasonId) {
        List<TrackingActionEntity> entities = trackingActionMapper.toEntityList(dtos);

        for (TrackingActionEntity entity : entities) {
            trackingActionRepository.save(entity);

            RiskTrackingActionEntity mapping = new RiskTrackingActionEntity();
            mapping.setRiskTrackingReasonId(riskTrackingReasonId);
            mapping.setTrackingActionId(entity.getId());
            riskTrackingActionRepository.save(mapping);
        }
    }

}
