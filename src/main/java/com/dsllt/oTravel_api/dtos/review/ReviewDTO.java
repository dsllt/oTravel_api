package com.dsllt.oTravel_api.dtos.review;

import com.dsllt.oTravel_api.dtos.place.PlaceDTO;
import com.dsllt.oTravel_api.entity.place.Place;
import com.dsllt.oTravel_api.entity.review.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
