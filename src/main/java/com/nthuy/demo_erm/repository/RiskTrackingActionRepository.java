package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTrackingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTrackingActionRepository extends JpaRepository<RiskTrackingActionEntity,Long> {
    List<RiskTrackingActionEntity> findByRiskTrackingReasonId(Long trackingReasonId);
}
