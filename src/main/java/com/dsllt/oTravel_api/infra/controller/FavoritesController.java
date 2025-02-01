package com.dsllt.oTravel_api.infra.controller;


import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import com.dsllt.oTravel_api.core.usecase.FavoriteService;
import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import com.dsllt.oTravel_api.infra.dto.favorite.UserFavoritesDTO;
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
public class FavoritesController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Favorite> create(@RequestBody @Valid CreateFavoriteDTO createFavoriteDTO){
        Favorite newFavorite = favoriteService.save(createFavoriteDTO);

        return ResponseEntity.status(201).body(newFavorite);
    }
    @GetMapping("/{userUuid}")
    public ResponseEntity<List<Favorite>> getByUserId(@NotNull @PathVariable UUID userUuid){
        List<Favorite> userFavorites = favoriteService.getByUserId(userUuid);

        return ResponseEntity.status(200).body(userFavorites);
    }

    @PutMapping
    public ResponseEntity<Favorite> update(@RequestBody @Valid CreateFavoriteDTO createFavoriteDTO){
        Favorite updatedFavorite = favoriteService.update(createFavoriteDTO);

        return ResponseEntity.ok().body(updatedFavorite);
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserFavoritesDTO>> getUsersWithFavorites(){
        List<UserFavoritesDTO> usersWithFavorites = favoriteService.getUsersWithActiveFavorites();

        return ResponseEntity.ok().body(usersWithFavorites);
    }
}


