package com.dsllt.oTravel_api.entity.place;

import com.dsllt.oTravel_api.dtos.enums.PlaceCategory;

import java.util.List;
import java.util.UUID;

public record PlaceFilter(
        UUID placeUuid,
        String name,
        String city,
        String country,
        List<PlaceCategory> category,
        String slug,
        Double rating
) {
}
