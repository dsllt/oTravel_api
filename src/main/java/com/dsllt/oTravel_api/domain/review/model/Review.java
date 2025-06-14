package com.dsllt.oTravel_api.domain.review.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Review(
        UUID id,
        String description,
        Double rating,
        UUID userId,
        UUID placeId,
        LocalDateTime createdAt
) {
}
