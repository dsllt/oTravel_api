package com.dsllt.oTravel_api.infra.dto.favorite;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFavoriteDTO(
        @NotNull(message = "O UUID do usuário deve ser definido.") UUID userId,
        @NotNull(message = "O UUID do local deve ser definido.") UUID placeId
        ) {}
