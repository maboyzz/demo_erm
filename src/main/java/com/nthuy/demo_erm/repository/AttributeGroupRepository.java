package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroupEntity, Long>, JpaSpecificationExecutor<AttributeGroupEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    List<AttributeGroupEntity> findByIdIn(Set<Long> ids);
}
