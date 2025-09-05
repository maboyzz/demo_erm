package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.AttributeEntity;
import org.springframework.data.jpa.domain.Specification;

public class AttributeSpecification {
    public static Specification<AttributeEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<AttributeEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<AttributeEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
    public static Specification<AttributeEntity> hasAttributeGroup(Long attributeGroupId) {
        return (root, query, cb) -> attributeGroupId == null ? null : cb.equal(root.get("attributeGroupId"), attributeGroupId);
    }
}
