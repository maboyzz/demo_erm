package com.nthuy.demo_erm.Repository;


import com.nthuy.demo_erm.Entity.ClassifyReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifyReasonRepository extends JpaRepository<ClassifyReasonEntity, Long> , JpaSpecificationExecutor<ClassifyReasonEntity> {

    boolean existsByName(String name);
}
