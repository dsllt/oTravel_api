package com.dsllt.oTravel_api.domain.place.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Place(
        UUID id,
        String name,
        String imageUrl,
        String description,
        String address,
        String city,
        String country,
        Double latitude,
        Double longitude,
        String slug,
        String phone,
        String[] category,
        Double rating,
        LocalDateTime createdAt
        ) {
}