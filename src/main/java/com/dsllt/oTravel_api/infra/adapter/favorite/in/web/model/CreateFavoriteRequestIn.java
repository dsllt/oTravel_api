package com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateFavoriteRequestIn(
        @NotNull(message = "O UUID do usuário deve ser definido.") UUID userId,
        @NotNull(message = "O UUID do local deve ser definido.") UUID placeId
        ) {}
