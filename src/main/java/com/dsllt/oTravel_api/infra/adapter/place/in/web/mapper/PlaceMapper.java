package com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper;

import com.dsllt.oTravel_api.domain.place.model.Place;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    @Mapping(target = "name", source = "placeEntity.name")
    @Mapping(target = "imageUrl", source = "placeEntity.imageUrl")
    @Mapping(target = "description", source = "placeEntity.description")
    @Mapping(target = "address", source = "placeEntity.address")
    @Mapping(target = "city", source = "placeEntity.city")
    @Mapping(target = "country", source = "placeEntity.country")
    @Mapping(target = "latitude", source = "placeEntity.latitude")
    @Mapping(target = "longitude", source = "placeEntity.longitude")
    @Mapping(target = "slug", source = "placeEntity.slug")
    @Mapping(target = "phone", source = "placeEntity.phone")
    @Mapping(target = "category", source = "placeEntity.category")
    @Mapping(target = "rating", source = "placeEntity.rating")
    @Mapping(target = "id", source = "placeEntity.id")
    @Mapping(target = "createdAt", source = "placeEntity.createdAt")
    Place toPlace(PlaceEntity placeEntity);
}
