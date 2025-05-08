package com.dsllt.oTravel_api.infra.dto.menu;

import jakarta.validation.constraints.NotNull;

public record EditMenuDTO(
        @NotNull(message = "O nome deve ser definido.") String name,
        @NotNull(message = "O preço deve ser definido.") Double price
) {
}
