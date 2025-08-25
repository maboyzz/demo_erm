package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.AttributeGroupEntity;

import org.springframework.data.jpa.domain.Specification;

public class AttributeGroupSpecification {
    public static Specification<AttributeGroupEntity> hasCode(String code) {
        return (root, query, cb) ->
                code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<AttributeGroupEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    public static Specification<AttributeGroupEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
