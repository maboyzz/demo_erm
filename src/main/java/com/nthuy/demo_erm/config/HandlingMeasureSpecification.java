package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.AttributeEntity;
import com.nthuy.demo_erm.entity.HandlingMeasureEntity;
import org.springframework.data.jpa.domain.Specification;

public class HandlingMeasureSpecification {

    public static Specification<HandlingMeasureEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<HandlingMeasureEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<HandlingMeasureEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

}


