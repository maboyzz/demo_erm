package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

            // Subquery đếm số systemIds khớp
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ClassifyReasonMapEntity> mapRoot = subquery.from(ClassifyReasonMapEntity.class);

            subquery.select(cb.count(mapRoot.get("systemId"))).where(cb.equal(mapRoot.get("classifyReasonId"), root.get("id")), mapRoot.get("systemId").in(systemIds));

            // Chỉ lấy reason có đủ TẤT CẢ systemIds
            return cb.equal(subquery, (long) systemIds.size());
        };
    }
}
