package com.dsllt.oTravel_api.infra.dto.menu;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.enums.MenuType;

import java.util.UUID;

public record MenuDTO(
        Long id,
        String name,
        MenuType type,
        Double price,
        UUID placeId
) {
    public static MenuDTO from(Menu menu) {
        return new MenuDTO(
                menu.getId(),
                menu.getName(),
                menu.getType(),
                menu.getPrice(),
                menu.getPlace().getId()
        );
    }
}
