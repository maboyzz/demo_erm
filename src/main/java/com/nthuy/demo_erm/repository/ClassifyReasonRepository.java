package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClassifyReasonRepository extends JpaRepository<ClassifyReasonEntity, Long> , JpaSpecificationExecutor<ClassifyReasonEntity> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    List<ClassifyReasonEntity> findByIdIn(Set<Long> ids);
}
