package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.AttributeRiskTypeEntity;
import com.nthuy.demo_erm.entity.RiskLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskLineRepository extends JpaRepository<RiskLineEntity,Long> {
    List<RiskLineEntity> findByRiskId(Long id);
    void deleteByRiskId(Long Id);
}
