package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.TagEntity;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class TrackingReasonSpecification {
    public static Specification<TrackingReasonEntity> hasIds(Set<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction(); // không filter nếu ids rỗng/null
            }
            return root.get("id").in(ids); // WHERE id IN (....)
        };
    }
}
