package com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model;

import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FavoriteByUserResponseOut(
        UUID favoriteUuid,
        PlaceResponseOut place,
        boolean active
) {

}
