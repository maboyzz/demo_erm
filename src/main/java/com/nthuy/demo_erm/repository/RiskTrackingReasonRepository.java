package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskEntity;
import com.nthuy.demo_erm.entity.RiskTrackingReasonEntity;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskTrackingReasonRepository extends JpaRepository<RiskTrackingReasonEntity,Long>
{
    List<RiskTrackingReasonEntity> findByRiskId(Long id);
}
