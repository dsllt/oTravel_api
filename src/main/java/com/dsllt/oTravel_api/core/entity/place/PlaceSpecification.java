package com.dsllt.oTravel_api.core.entity.place;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PlaceSpecification {

    public static Specification<Place> withFilter(PlaceFilter placeFilter){

        return  (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (placeFilter.name() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), placeFilter.name()));
            }

            if (placeFilter.city() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), placeFilter.city()));
            }

            if (placeFilter.country() != null) {
                predicates.add(criteriaBuilder.equal(root.get("country"), placeFilter.country()));
            }

            if (placeFilter.category() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), placeFilter.category()));
            }

            if (placeFilter.slug() != null) {
                predicates.add(criteriaBuilder.equal(root.get("slug"), placeFilter.slug()));
            }

            if (placeFilter.rating() != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), placeFilter.rating()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
