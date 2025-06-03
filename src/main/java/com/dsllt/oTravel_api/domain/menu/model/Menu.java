package com.dsllt.oTravel_api.domain.menu.model;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Menu(
        Long id,
        String name,
        MenuType type,
        Double price,
        UUID placeId,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
}
