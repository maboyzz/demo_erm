package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, Long> {
    List<AttributeValueEntity> findByAttributeId(Long attributeId);
    void deleteByAttributeId(Long attributeId);
}
