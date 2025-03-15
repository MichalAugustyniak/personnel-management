package com.pm.personnelmanagement.common.util;

import com.pm.personnelmanagement.user.model.Address;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {
    public static <T, L> Specification<L> addCriteria(Specification<L> specification, T value, String propertyName) {
        if (value == null) {
            return specification;
        }
        Specification<L> hasProperty = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(propertyName), value);
        return specification.and(hasProperty);
    }
}
