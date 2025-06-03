package com.dsllt.oTravel_api.infra.adapter.favorite.in.web;


import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.domain.favorite.service.FavoriteService;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.mapper.FavoriteResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUserResponseOut;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteResponseOut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favorite")
@AllArgsConstructor
public class FavoriteRest {

    private final FavoriteService favoriteService;
    private final FavoriteResponseOutMapper favoriteResponseOutMapper;

    @PostMapping
    public ResponseEntity<Favorite> create(@RequestBody @Valid CreateFavoriteRequestIn favoriteRequestIn) {
        Favorite newFavorite = favoriteService.create(favoriteRequestIn);

        return ResponseEntity.status(201).body(newFavorite);
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<FavoriteResponseOut> getByUserId(@NotNull @PathVariable UUID userUuid) {
        var userFavorites = favoriteService.getByUserId(userUuid);
        var responseOut = favoriteResponseOutMapper.toFavoriteResponseOut(userFavorites);
        return ResponseEntity.ok().body(responseOut);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Favorite> update(@NotNull @PathVariable UUID userId, @RequestParam UUID placeId) {
        var updatedFavorite = favoriteService.update(userId, placeId);
        return ResponseEntity.ok().body(updatedFavorite);
    }

    @GetMapping("/active")
    public ResponseEntity<List<FavoriteResponseOut>> getUsersWithFavorites() {
        List<FavoriteByUser> usersWithFavorites = favoriteService.getUsersWithActiveFavorites();
        var responseOut = usersWithFavorites.stream()
                .map(favoriteResponseOutMapper::toFavoriteResponseOut)
                .toList();
        return ResponseEntity.ok().body(responseOut);
    }
}


