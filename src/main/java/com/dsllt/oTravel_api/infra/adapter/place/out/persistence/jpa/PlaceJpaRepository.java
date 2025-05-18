package com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, UUID>, JpaSpecificationExecutor<PlaceEntity> {

    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
