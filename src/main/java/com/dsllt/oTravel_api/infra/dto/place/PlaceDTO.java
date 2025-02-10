package com.dsllt.oTravel_api.infra.dto.place;

import com.dsllt.oTravel_api.infra.enums.PlaceCategory;
import com.dsllt.oTravel_api.core.entity.place.Place;

import java.util.List;
import java.util.UUID;

public record PlaceDTO(
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
) {
    public static PlaceDTO from(Place place) {
        return new PlaceDTO(
                place.getId(),
                place.getName(),
                place.getImageUrl(),
                place.getDescription(),
                place.getAddress(),
                place.getCity(),
                place.getCountry(),
                place.getLatitude(),
                place.getLongitude(),
                place.getSlug(),
                place.getPhone(),
                place.getCategoryList(),
                place.getRating()
        );
    }
    public PlaceDTO(Place place) {
        this(place.getId(),
                place.getName(),
                place.getImageUrl(),
                place.getDescription(),
                place.getAddress(),
                place.getCity(),
                place.getCountry(),
                place.getLatitude(),
                place.getLongitude(),
                place.getSlug(),
                place.getPhone(),
                place.getCategoryList(),
                place.getRating());
    }
}
