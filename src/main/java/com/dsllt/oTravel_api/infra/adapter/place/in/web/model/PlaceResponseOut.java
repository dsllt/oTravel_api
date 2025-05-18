package com.dsllt.oTravel_api.infra.adapter.place.in.web.model;

import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record PlaceResponseOut(
        UUID id,
        String name,
        String imageUrl,
        String description,
        String address,
        String city,
        String country,
        Double latitude,
        Double longitude,
        String slug,
        String phone,
        List<PlaceCategory> category,
        Double rating
) { }
