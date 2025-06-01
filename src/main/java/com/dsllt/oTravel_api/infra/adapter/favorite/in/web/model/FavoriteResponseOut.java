package com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model;

import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record FavoriteResponseOut(
        CreateUserResponseOut user,
        List<PlaceResponseOut> favorites
) {
}
