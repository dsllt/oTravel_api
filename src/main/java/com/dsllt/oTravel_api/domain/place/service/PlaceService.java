package com.dsllt.oTravel_api.domain.place.service;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.domain.place.port.in.PlaceUseCase;
import com.dsllt.oTravel_api.domain.place.port.out.PlacePort;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper.PlaceResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService implements PlaceUseCase {

    private final PlacePort placePort;
    private final PlaceResponseOutMapper placeResponseOutMapper;

    @Override
    public Place create(CreatePlaceRequestIn createPlaceRequestIn) {
        return placePort.create(createPlaceRequestIn);
    }

    @Override
    public List<PlaceResponseOut> get() {
        var placeList = placePort.get();

        return placeList.stream()
                .map(placeResponseOutMapper::toPlaceResponseOut)
                .toList();
    }

    @Override
    public PlaceResponseOut getById(UUID placeUuid) {
        var place = placePort.getById(placeUuid);
        return placeResponseOutMapper.toPlaceResponseOut(place);
    }

    @Override
    public PlaceResponseOut update(UUID placeUuid, UpdatePlaceRequestIn updatePlaceRequestIn) {
        var place = placePort.update(placeUuid, updatePlaceRequestIn);
        return placeResponseOutMapper.toPlaceResponseOut(place);
    }
}
