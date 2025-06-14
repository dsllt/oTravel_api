package com.dsllt.oTravel_api.infra.adapter.favorite.in.web.mapper;

import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteResponseOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteResponseOutMapper {

    FavoriteResponseOut toFavoriteResponseOut(FavoriteByUser favoriteByUser);
}
