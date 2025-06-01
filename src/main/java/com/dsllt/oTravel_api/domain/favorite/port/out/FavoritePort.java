package com.dsllt.oTravel_api.domain.favorite.port.out;

import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;

import java.util.List;
import java.util.UUID;

public interface FavoritePort {

    Favorite create(CreateFavoriteRequestIn favoriteRequestIn);

    FavoriteByUser getByUserId(UUID userUuid);

    Favorite update(UUID userUuid, UUID placeUuid);

    List<FavoriteByUser> getUsersWithActiveFavorites();
}
