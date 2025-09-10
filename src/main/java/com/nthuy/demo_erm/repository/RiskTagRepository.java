package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTagEntity;
import com.nthuy.demo_erm.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskTagRepository extends JpaRepository<RiskTagEntity, Long> {
    List<RiskTagEntity> findByRiskId(Long id);
}
