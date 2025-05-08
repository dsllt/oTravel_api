package com.dsllt.oTravel_api.infra.adapter.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteJpaRepository extends JpaRepository<Favorite, UUID> {
    Favorite findByUserIdAndPlaceId(UUID userId, UUID placeId);
    boolean existsByUserIdAndPlaceId(UUID userId, UUID placeId);
    List<Favorite> findAllByUserIdAndActiveTrue(UUID userId);
    List<Favorite> findAllByActiveTrue();
}
