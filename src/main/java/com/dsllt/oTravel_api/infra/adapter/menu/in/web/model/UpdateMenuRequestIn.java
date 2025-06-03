package com.dsllt.oTravel_api.infra.adapter.menu.in.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateMenuRequestIn(
        @NotNull(message = "O nome deve ser definido.") String name,
        @NotNull(message = "O preço deve ser definido.") Double price
) {
}
