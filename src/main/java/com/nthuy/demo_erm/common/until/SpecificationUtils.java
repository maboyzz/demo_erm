package com.nthuy.demo_erm.common.until;

import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class SpecificationUtils {

    /**
     * Thêm spec khi string không null/rỗng
     */
    public static <T> Specification<T> addIfNotBlank(
            Specification<T> spec,
            String value,
            Function<String, Specification<T>> specFunction) {
        if (value != null && !value.isBlank()) {
            return spec.and(specFunction.apply(value));
        }
        return spec;
    }

    /**
     * Thêm spec khi collection không null/rỗng
     */
    public static <T, U> Specification<T> addIfNotEmpty(
            Specification<T> spec,
            Collection<U> values,
            Function<Collection<U>, Specification<T>> specFunction) {
        if (values != null && !values.isEmpty()) {
            return spec.and(specFunction.apply(values));
        }
        return spec;
    }
//    public static <T> Specification<T> addIfNotNull(
//            Specification<T> spec,
//            Boolean value,
//            Function<Boolean, Specification<T>> specFunction) {
//        if (value != null) {
//            return spec.and(specFunction.apply(value));
//        }
//        return spec;
//    }
    public static <T, U> Specification<T> addIfNotNull(
            Specification<T> spec,
            U value,
            Function<U, Specification<T>> specFunction) {
        if (value != null) {
            return spec.and(specFunction.apply(value));
        }
        return spec;
    }
}