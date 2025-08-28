package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ClassifyReasonSpecification {

    public static Specification<ClassifyReasonEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<ClassifyReasonEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ClassifyReasonEntity> hasSystemIdIn(List<Long> systemIds) {
        return (root, query, cb) -> {
            if (systemIds == null || systemIds.isEmpty()) {
                return null;
            }
            // Join với bảng trung gian ClassifyReasonMapEntity
            Join<ClassifyReasonEntity, ClassifyReasonMapEntity> mapJoin = root.join("classifyReasonMaps");

            // Điều kiện: systemId phải nằm trong danh sách systemIds
            Predicate systemIdInPredicate = mapJoin.get("systemId").in(systemIds);

            // Tránh duplicate records do join
            query.distinct(true);

            return systemIdInPredicate;
        };
    }
}
