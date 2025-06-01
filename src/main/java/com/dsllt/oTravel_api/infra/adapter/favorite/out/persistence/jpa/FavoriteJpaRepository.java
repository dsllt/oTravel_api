package com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, UUID> {
    FavoriteEntity findByUserIdAndPlaceId(UUID userId, UUID placeId);
    FavoriteEntity findByUserId(UUID userId);
    boolean existsByUserIdAndPlaceId(UUID userId, UUID placeId);
    List<FavoriteEntity> findAllByUserIdAndActiveTrue(UUID userId);
    List<FavoriteEntity> findAllByActiveTrue();
}
