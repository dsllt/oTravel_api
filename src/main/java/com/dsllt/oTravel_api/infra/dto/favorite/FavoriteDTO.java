package com.dsllt.oTravel_api.infra.dto.favorite;

import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;

import java.util.UUID;

public record FavoriteDTO(
        UUID favoriteUUID,
        PlaceDTO place,
        boolean active
) {
    public FavoriteDTO(Favorite favorite){
        this(favorite.getId(), PlaceDTO.from(favorite.getPlace()), favorite.isActive());
    }
}
