package com.dsllt.oTravel_api.infra.adapter.menu.in.web.model;

import com.dsllt.oTravel_api.domain.menu.model.MenuType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MenuResponseOut(
        Long id,
        String name,
        MenuType type,
        Double price,
        UUID placeId
) {}
