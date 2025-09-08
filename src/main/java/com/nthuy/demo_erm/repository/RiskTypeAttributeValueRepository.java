package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.AttributeRiskTypeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskTypeAttributeValueRepository extends JpaRepository<AttributeRiskTypeValueEntity,Long> {
    List<AttributeRiskTypeValueEntity> findByRiskTypeAttributeId(Long attributeRiskTypeId);
}
