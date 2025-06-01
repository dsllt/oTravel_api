package com.dsllt.oTravel_api.domain.favorite.service;

import com.dsllt.oTravel_api.domain.favorite.port.in.FavoriteUseCase;
import com.dsllt.oTravel_api.domain.favorite.port.out.FavoritePort;
import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUserResponseOut;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteResponseOut;
import com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa.FavoriteJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FavoriteService implements FavoriteUseCase {

    private final FavoritePort favoritePort;

    @Override
    public Favorite create(@Valid CreateFavoriteRequestIn favoriteRequestIn){
       return favoritePort.create(favoriteRequestIn);
    }

    @Override
    public FavoriteByUser getByUserId(UUID userUuid){
        return favoritePort.getByUserId(userUuid);
    }

    @Override
    public Favorite update(UUID userUuid, UUID placeUuid) {
        return favoritePort.update(userUuid, placeUuid);
    }

    @Override
    public List<FavoriteByUser> getUsersWithActiveFavorites(){
        return favoritePort.getUsersWithActiveFavorites();
    }
}
