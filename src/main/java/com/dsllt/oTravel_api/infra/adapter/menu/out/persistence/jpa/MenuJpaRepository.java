package com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, Long> {
    boolean existsByNameAndPlaceId(String name, UUID placeId);
    boolean existsByPlaceId(UUID placeId);
    List<MenuEntity> findAllByPlaceId(UUID placeId);
}
