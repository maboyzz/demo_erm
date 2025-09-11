package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.dto.TrackingActionDTO;
import com.nthuy.demo_erm.entity.TrackingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingActionRepository extends JpaRepository <TrackingActionEntity, Long>{
    @Query("SELECT ta FROM TrackingActionEntity ta " +
            "JOIN RiskTrackingActionEntity rta ON ta.id = rta.trackingActionId " +
            "JOIN RiskTrackingReasonEntity rtr ON rtr.id = rta.riskTrackingReasonId " +
            "WHERE rtr.riskId = :riskId AND rtr.trackingReasonId = :trackingReasonId")
    List<TrackingActionEntity> findByRiskIdAndTrackingReasonId(Long riskId, Long trackingReasonId);

}
