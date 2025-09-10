package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.RiskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskRepository extends JpaRepository<RiskEntity,Long> , JpaSpecificationExecutor<RiskEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
