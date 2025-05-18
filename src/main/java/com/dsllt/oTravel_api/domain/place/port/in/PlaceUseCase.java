package com.dsllt.oTravel_api.domain.place.port.in;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;

import java.util.List;
import java.util.UUID;

public interface PlaceUseCase {

    Place create(CreatePlaceRequestIn createPlaceRequestIn);
    List<PlaceResponseOut> get();
    PlaceResponseOut getById(UUID placeUuid);
    PlaceResponseOut update(UUID placeUuid, UpdatePlaceRequestIn updatePlaceRequestIn);
}
