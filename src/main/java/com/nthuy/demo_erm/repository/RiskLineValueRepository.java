package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskLineValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskLineValueRepository extends JpaRepository<RiskLineValueEntity,Long> {
    List<RiskLineValueEntity> findByRiskLineId(Long id);

}
