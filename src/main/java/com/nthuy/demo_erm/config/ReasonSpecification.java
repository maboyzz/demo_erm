package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.constant.EnumTypeReason;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ReasonSpecification {
    public static Specification<ReasonEntity> hasCode(String code) {
        return (root, query, cb) ->
                code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<ReasonEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ReasonEntity> hasSystemIdIn(List<Long> systemIds) {
        return (root, query, cb) -> {
            if (systemIds == null || systemIds.isEmpty()) return null;

            // Join với bảng trung gian
            Join<ReasonEntity, SystemEntity> systemJoin = root.join("systemEntitiesReason");

            // Tránh duplicate khi join
            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.countDistinct(systemJoin.get("id")), systemIds.size()));

            return systemJoin.get("id").in(systemIds);
        };
    }
    public static Specification<ReasonEntity> hasType(EnumTypeReason type) {
        return (root, query, cb) -> {
            if (type == null) {
                return null; // Không filter => lấy cả 2
            }
            return cb.equal(root.get("type"), type);
        };
    }
    public static Specification<ReasonEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
