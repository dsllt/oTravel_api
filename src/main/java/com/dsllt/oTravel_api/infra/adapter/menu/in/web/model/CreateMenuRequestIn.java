package com.dsllt.oTravel_api.infra.adapter.menu.in.web.model;

import com.dsllt.oTravel_api.domain.menu.model.MenuType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMenuRequestIn(
        @NotNull(message = "O nome deve ser definido.") String name,
        @NotNull(message = "O tipo de item no menu deve ser definido.") MenuType type,
        @NotNull(message = "O preço deve ser definido.") Double price,
        @NotNull(message = "O local deve ser definido.") UUID placeId
) {
}
