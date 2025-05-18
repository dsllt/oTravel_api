package com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceResponseOutMapper {

    PlaceResponseOut toPlaceResponseOut(Place place);
}
