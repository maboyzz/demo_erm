package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifyReasonRepository extends JpaRepository<ClassifyReasonEntity, Long> , JpaSpecificationExecutor<ClassifyReasonEntity> {

    boolean existsByName(String name);
}
