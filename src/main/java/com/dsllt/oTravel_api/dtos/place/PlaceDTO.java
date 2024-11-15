package com.dsllt.oTravel_api.dtos.place;

import com.dsllt.oTravel_api.dtos.enums.PlaceCategory;
import com.dsllt.oTravel_api.entity.place.Place;

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
                place.getCategory(),
                place.getRating()
        );
    }
}
