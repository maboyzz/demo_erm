package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.RiskTypeEntity;
import com.nthuy.demo_erm.entity.RiskTypeMapEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class RiskTypeSpecification {

    public static Specification<RiskTypeEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<RiskTypeEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<RiskTypeEntity> hasSystemIdIn(Collection<Long> systemIds) {
        return (root, query, cb) -> {
            if (systemIds == null || systemIds.isEmpty()) {
                return null; // không thêm filter
            }

            // Subquery: đếm số systemId mapping khớp với entity hiện tại
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<RiskTypeMapEntity> mapRoot = subquery.from(RiskTypeMapEntity.class);

            subquery.select(cb.count(mapRoot.get("systemId")))
                    .where(
                            cb.equal(mapRoot.get("riskTypeId"), root.get("id")),
                            mapRoot.get("systemId").in(systemIds)
                    );

            // Điều kiện: số lượng systemIds khớp phải đúng bằng size của systemIds truyền vào
            return cb.equal(subquery, (long) systemIds.size());
        };
    }
    public static Specification<RiskTypeEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
