package com.dsllt.oTravel_api.core.entity.review;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification {
    public static Specification<Review> withFilter(ReviewFilter reviewFilter){

        return  (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (reviewFilter.userUuid() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user"), reviewFilter.userUuid()));
            }

            if (reviewFilter.placeUuid() != null) {
                predicates.add(criteriaBuilder.equal(root.get("place"), reviewFilter.placeUuid()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
