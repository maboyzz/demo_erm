package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.RiskTypeEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskTypeRepository extends JpaRepository<RiskTypeEntity,Long>, JpaSpecificationExecutor<RiskTypeEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    @Query("""
        SELECT rt 
        FROM RiskTypeEntity rt
        WHERE rt.id = :id
    """)
    Optional<RiskTypeEntity> findDetailById(@Param("id") Long id);
}
