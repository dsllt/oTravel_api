package com.dsllt.oTravel_api.infra.dto.menu;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.enums.MenuType;
import jakarta.validation.constraints.NotNull;

public record CreateMenuDTO(
        @NotNull(message = "O nome deve ser definido.") String name,
        @NotNull(message = "O tipo de item no menu deve ser definido.") MenuType type,
        @NotNull(message = "O preço deve ser definido.")Double price,
        @NotNull(message = "O local deve ser definido.") Place place
) {
}
