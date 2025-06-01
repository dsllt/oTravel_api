package com.dsllt.oTravel_api.domain.favorite.model;

import lombok.Builder;

import java.util.UUID;


@Builder
public record Favorite(
        UUID id,
        UUID userId,
        UUID placeId,
        boolean active
        ) {
}
