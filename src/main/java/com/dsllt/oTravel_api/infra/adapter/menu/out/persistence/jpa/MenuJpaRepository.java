package com.dsllt.oTravel_api.infra.adapter.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
    boolean existsByNameAndPlaceId(String name, UUID placeId);
    boolean existsByPlaceId(UUID placeId);
    List<Menu> findAllByPlaceId(UUID placeId);
}
