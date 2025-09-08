package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTypeEntity;
import com.nthuy.demo_erm.entity.SampleActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleActionRepository extends JpaRepository<SampleActionEntity, Long>, JpaSpecificationExecutor<SampleActionEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
