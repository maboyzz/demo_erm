package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskFileRepository extends JpaRepository<RiskFileEntity,Long> {
    List<RiskFileEntity> findByRiskId(Long id);
}
