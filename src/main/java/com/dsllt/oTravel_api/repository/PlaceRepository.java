package com.dsllt.oTravel_api.repository;

import com.dsllt.oTravel_api.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID>, JpaSpecificationExecutor<Place> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
