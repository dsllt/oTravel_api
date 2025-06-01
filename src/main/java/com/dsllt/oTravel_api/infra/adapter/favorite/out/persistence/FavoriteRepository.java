package com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence;

import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.domain.favorite.port.out.FavoritePort;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteByUser;
import com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa.FavoriteEntity;
import com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa.FavoriteJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.mapper.FavoriteMapper;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper.PlaceMapper;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.mapper.UserMapper;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class FavoriteRepository implements FavoritePort {

    private final FavoriteJpaRepository favoriteJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserMapper userMapper;
    private final PlaceMapper placeMapper;

    @Override
    public Favorite create(CreateFavoriteRequestIn favoriteRequestIn) {
        if (favoriteJpaRepository.existsByUserIdAndPlaceId(favoriteRequestIn.userId(), favoriteRequestIn.placeId())) {
            throw new BusinessException("Favorito já incluído");
        }
        var user = userJpaRepository.findById(favoriteRequestIn.userId()).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
        var place = placeJpaRepository.findById(favoriteRequestIn.placeId()).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado."));
        var newFavorite = FavoriteEntity.builder()
                .userId(user.getId())
                .placeId(place.getId())
                .active(true)
                .build();
        var savedFavorite = favoriteJpaRepository.save(newFavorite);
        return favoriteMapper.toFavorite(savedFavorite);
    }

    @Override
    public FavoriteByUser getByUserId(UUID userUuid) {
        var userEntity = userJpaRepository.findById(userUuid).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
        var usersFavorites = favoriteJpaRepository.findAllByUserIdAndActiveTrue(userUuid);
        var placesEntity = usersFavorites.stream()
                .map(favorite -> placeJpaRepository.findById(favorite.getPlaceId()).orElse(null))
                .toList();
        var user = userMapper.toUser(userEntity);
        var places = placesEntity.stream().map(placeMapper::toPlace).toList();
        return FavoriteByUser.builder()
                .user(user)
                .favorites(places)
                .build();
    }

    @Override
    public Favorite update(UUID userUuid, UUID placeUuid) {
        var savedFavorite = favoriteJpaRepository.findByUserIdAndPlaceId(userUuid, placeUuid);
        savedFavorite.updateActiveStatus();
        var updatedFavorite = favoriteJpaRepository.save(savedFavorite);
        return favoriteMapper.toFavorite(updatedFavorite);
    }

    @Override
    public List<FavoriteByUser> getUsersWithActiveFavorites() {
        List<FavoriteEntity> activeFavorites = favoriteJpaRepository.findAllByActiveTrue();
        // 1. Agrupar por usuario
        Map<UUID, List<FavoriteEntity>> favoritesByUser = activeFavorites.stream()
                .collect(Collectors.groupingBy(FavoriteEntity::getUserId));

        return favoritesByUser.entrySet().stream()
                .map(entry -> {
                    UUID userId = entry.getKey();
                    List<FavoriteEntity> userFavorites = entry.getValue();

                    // 2. Busca do usuario
                    var userEntity = userJpaRepository.findById(userId)
                            .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

                    // 3. Busca de cada lugar
                    var placesEntity = userFavorites.stream()
                            .map(favorite -> placeJpaRepository.findById(favorite.getPlaceId()).orElse(null))
                            .filter(java.util.Objects::nonNull) // Remove lugares que não foram encontrados
                            .toList();

                    // 4. Conversão para entidades correspondentes
                    var user = userMapper.toUser(userEntity);
                    var places = placesEntity.stream().map(placeMapper::toPlace).toList();

                    // 5. Criação do objeto de resposta
                    return FavoriteByUser.builder()
                            .user(user)
                            .favorites(places)
                            .build();
                })
                .filter(java.util.Objects::nonNull) // Remove entradas nulas caso o usuário não tenha sido encontrado
                .toList();
    }
}
