package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTrackingReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskTrackingReasonRepository extends JpaRepository<RiskTrackingReasonEntity,Long>
{
}
