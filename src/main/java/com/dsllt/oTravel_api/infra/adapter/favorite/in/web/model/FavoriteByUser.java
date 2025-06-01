package com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model;

import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.domain.user.model.User;
import lombok.Builder;

import java.util.List;

@Builder
public record FavoriteByUser(
        User user,
        List<Place> favorites
) {
}
