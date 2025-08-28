package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public class ReasonSpecification {
    public static Specification<ReasonEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<ReasonEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ReasonEntity> hasSystemIdIn(Collection<Long> systemIds) {
        return (root, query, cb) -> {
            if (systemIds == null || systemIds.isEmpty()) {
                return null; // không thêm filter
            }
            // Subquery: đếm số systemId mapping khớp với entity hiện tại
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ReasonMapEntity> mapRoot = subquery.from(ReasonMapEntity.class);

            subquery.select(cb.count(mapRoot.get("systemId")))
                    .where(
                            cb.equal(mapRoot.get("reasonId"), root.get("id")),
                            mapRoot.get("systemId").in(systemIds)
                    );

            // Điều kiện: số lượng systemIds khớp phải đúng bằng size của systemIds truyền vào
            return cb.equal(subquery, (long) systemIds.size());
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
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
