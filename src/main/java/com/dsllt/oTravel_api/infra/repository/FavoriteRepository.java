package com.dsllt.oTravel_api.infra.repository;

import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Favorite findByUserIdAndPlaceId(UUID userId, UUID placeId);
    boolean existsByUserIdAndPlaceId(UUID userId, UUID placeId);
    List<Favorite> findAllByUserId(UUID userId);
    List<Favorite> findAllByActiveTrue();
}
