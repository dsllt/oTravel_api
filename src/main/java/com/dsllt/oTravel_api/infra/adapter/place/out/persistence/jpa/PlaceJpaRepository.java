package com.dsllt.oTravel_api.infra.adapter.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaceJpaRepository extends JpaRepository<Place, UUID>, JpaSpecificationExecutor<Place> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
