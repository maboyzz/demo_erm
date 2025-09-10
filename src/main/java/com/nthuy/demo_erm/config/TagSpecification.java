package com.nthuy.demo_erm.config;

import com.nthuy.demo_erm.entity.RiskTypeEntity;
import com.nthuy.demo_erm.entity.TagEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class TagSpecification {
    public static Specification<TagEntity> hasIds(Set<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction(); // không filter nếu ids rỗng/null
            }
            return root.get("id").in(ids); // WHERE id IN (....)
        };
    }
}
