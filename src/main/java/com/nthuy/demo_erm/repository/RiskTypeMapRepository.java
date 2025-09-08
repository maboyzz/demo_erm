package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskCategoryMapEntity;
import com.nthuy.demo_erm.entity.RiskTypeMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskTypeMapRepository extends JpaRepository<RiskTypeMapEntity,Long> {

    List<RiskTypeMapEntity> findByRiskTypeId(Long riskTypeId);

    void deleteByRiskTypeId(Long riskTypeId);
    List<RiskTypeMapEntity> findByRiskTypeIdIn(List<Long> riskTypeId);
}
