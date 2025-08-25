package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategoryEntity, Long>, JpaSpecificationExecutor<RiskCategoryEntity> {
}
