package com.dsllt.oTravel_api.infra.dto.review;

import com.dsllt.oTravel_api.core.entity.review.Review;

public record ReviewDTO(
        String description,
        Double rating,
        String placeName,
        String userFirstName,
        String userLastName
) {
    public static ReviewDTO from(Review review) {
        return new ReviewDTO(
                review.getDescription(),
                review.getRating(),
                review.getPlace().getName(),
                review.getUser().getFirstName(),
                review.getUser().getLastName()
        );
    }
}
