package com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.mapper;

import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa.FavoriteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    Favorite toFavorite(FavoriteEntity favoriteEnity);
}
