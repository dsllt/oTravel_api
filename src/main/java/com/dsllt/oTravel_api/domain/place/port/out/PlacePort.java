package com.dsllt.oTravel_api.domain.place.port.out;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;

import java.util.List;
import java.util.UUID;

public interface PlacePort {
    List<Place> get();
    Place getById(UUID placeUuid);
    Place create(CreatePlaceRequestIn createPlaceRequestIn);
    Place update(UUID placeUuid, UpdatePlaceRequestIn updatePlaceRequestIn);
}
