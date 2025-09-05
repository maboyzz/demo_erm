package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.ReasonMapEntity;
import com.nthuy.demo_erm.entity.RiskCategoryMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskCategoryMapRepository extends JpaRepository<RiskCategoryMapEntity, Long> {
    List<RiskCategoryMapEntity> findByRiskCategoryId(Long riskCategoryId);

    void deleteByRiskCategoryId(Long riskCategoryId);
    List<RiskCategoryMapEntity> findByRiskCategoryIdIn(List<Long> reasonIds);
}
