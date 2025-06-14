package com.dsllt.oTravel_api.infra.adapter.review.in.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateReviewRequestIn(
        String description,
        Double rating
) {
}
