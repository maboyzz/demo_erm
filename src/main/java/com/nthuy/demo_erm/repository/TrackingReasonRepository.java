package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTrackingReasonEntity;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrackingReasonRepository extends JpaRepository<TrackingReasonEntity, Long> , JpaSpecificationExecutor<TrackingReasonEntity>
{


}
