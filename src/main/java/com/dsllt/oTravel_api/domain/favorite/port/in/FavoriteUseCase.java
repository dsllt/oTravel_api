package com.dsllt.oTravel_api.domain.favorite.port.in;

import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUserResponseOut;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteResponseOut;

import java.util.List;
import java.util.UUID;

public interface FavoriteUseCase {

    Favorite create(CreateFavoriteRequestIn favoriteRequestIn);

    FavoriteByUser getByUserId(UUID userUuid);

    Favorite update(UUID userUuid, UUID placeUuid);

    List<FavoriteByUser> getUsersWithActiveFavorites();
}
