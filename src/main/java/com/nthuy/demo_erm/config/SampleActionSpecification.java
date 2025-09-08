package com.nthuy.demo_erm.config;


import com.nthuy.demo_erm.entity.SampleActionEntity;
import org.springframework.data.jpa.domain.Specification;

public class SampleActionSpecification {
    public static Specification<SampleActionEntity> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<SampleActionEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<SampleActionEntity> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
    public static Specification<SampleActionEntity> hasRiskType(Long riskTypeId) {
        return (root, query, cb) -> riskTypeId == null ? null : cb.equal(root.get("riskTypeId"), riskTypeId);
    }
    public static Specification<SampleActionEntity> hasClassifyReason(Long classifyReasonId) {
        return (root, query, cb) -> classifyReasonId == null ? null : cb.equal(root.get("classifyReasonId"), classifyReasonId);
    }
}
