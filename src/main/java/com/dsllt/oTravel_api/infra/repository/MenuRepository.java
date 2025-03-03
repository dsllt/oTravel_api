package com.dsllt.oTravel_api.infra.repository;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByNameAndPlaceId(String name, UUID placeId);
    boolean existsByPlaceId(UUID placeId);
    Menu[] findByPlaceId(UUID placeId);
}
