package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;
import com.nthuy.demo_erm.entity.AttributeGroupEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroupEntity, Long>, JpaSpecificationExecutor<AttributeGroupEntity> {
    boolean existsByName(String name);
}
