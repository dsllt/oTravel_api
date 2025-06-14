package com.dsllt.oTravel_api.infra.adapter.place.out.persistence;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import com.dsllt.oTravel_api.domain.place.port.out.PlacePort;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper.PlaceMapper;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceEntity;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.mapper.PlaceEntityMapper;
import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class PlaceRepository implements PlacePort {

    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceEntityMapper placeEntityMapper;
    private final PlaceMapper placeMapper;

    @Override
    public Place create(CreatePlaceRequestIn createPlaceRequestIn) {
        if (placeJpaRepository.existsByName(createPlaceRequestIn.name()) ||
                placeJpaRepository.existsBySlug(createPlaceRequestIn.slug())) {
            throw new BusinessException("Lugar já cadastrado.");
        }
        var newPlace = placeEntityMapper.toPlaceEntity(createPlaceRequestIn);
        var savedPlace = placeJpaRepository.save(newPlace);
        return placeMapper.toPlace(savedPlace);
    }

    @Override
    public List<Place> get() {
        var places = placeJpaRepository.findAll();
        return places.stream()
                .map(placeMapper::toPlace)
                .toList();
    }

    @Override
    public Place getById(UUID placeUuid) {
        PlaceEntity retrievedPlace = placeJpaRepository
                .findById(placeUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Lugar não encontrado."));
        return placeMapper.toPlace(retrievedPlace);
    }

    @Override
    public Place update(UUID placeUuid, UpdatePlaceRequestIn updatePlaceRequestIn) {
        var updatedPlaceData = validateUpdatePlaceData(placeUuid, updatePlaceRequestIn);
        var savedPlace = placeJpaRepository.save(updatedPlaceData);
        return placeMapper.toPlace(savedPlace);
    }

    private PlaceEntity validateUpdatePlaceData(UUID placeUuid, UpdatePlaceRequestIn updatePlaceRequestIn) {
        PlaceEntity retrievedPlace = placeJpaRepository.findById(placeUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Lugar não encontrado."));

        updateIfNotBlank(updatePlaceRequestIn.name(), retrievedPlace::setName);
        updateIfNotBlank(updatePlaceRequestIn.imageUrl(), retrievedPlace::setImageUrl);
        updateIfNotBlank(updatePlaceRequestIn.description(), retrievedPlace::setDescription);
        updateIfNotBlank(updatePlaceRequestIn.address(), retrievedPlace::setAddress);
        updateIfNotBlank(updatePlaceRequestIn.city(), retrievedPlace::setCity);
        updateIfNotBlank(updatePlaceRequestIn.country(), retrievedPlace::setCountry);
        updateIfNotNull(updatePlaceRequestIn.latitude(), retrievedPlace::setLatitude);
        updateIfNotNull(updatePlaceRequestIn.longitude(), retrievedPlace::setLongitude);
        updateIfNotBlank(updatePlaceRequestIn.slug(), retrievedPlace::setSlug);
        updateIfNotBlank(updatePlaceRequestIn.phone(), retrievedPlace::setPhone);

        if (updatePlaceRequestIn.category() != null && !updatePlaceRequestIn.category().isEmpty()) {
            String[] categoryArray = updatePlaceRequestIn.category().stream()
                    .map(Enum::name)
                    .toArray(String[]::new);
            retrievedPlace.setCategory(categoryArray);
        }

        return retrievedPlace;
    }

    private void updateIfNotBlank(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
