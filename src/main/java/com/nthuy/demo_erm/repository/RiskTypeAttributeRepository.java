package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.AttributeRiskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskTypeAttributeRepository extends JpaRepository<AttributeRiskTypeEntity,Long> {
    List<AttributeRiskTypeEntity> findByRiskTypeId(Long riskTypeId);
    void deleteByRiskTypeId(Long Id);
}
