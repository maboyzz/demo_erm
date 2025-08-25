package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RiskCategorySpecification {
    public static Specification<RiskCategoryEntity> hasCode(String code) {
        return (root, query, cb) ->
                code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<RiskCategoryEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<RiskCategoryEntity> hasSystemIdIn(List<Long> systemIds) {
        return (root, query, cb) -> {
            if (systemIds == null || systemIds.isEmpty()) return null;

            // Join với bảng trung gian
            Join<RiskCategoryEntity, SystemEntity> systemJoin = root.join("systemEntitiesRiskCategory");

            // Tránh duplicate khi join
            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.countDistinct(systemJoin.get("id")), systemIds.size()));

            return systemJoin.get("id").in(systemIds);
        };
    }
    public static Specification<RiskCategoryEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
