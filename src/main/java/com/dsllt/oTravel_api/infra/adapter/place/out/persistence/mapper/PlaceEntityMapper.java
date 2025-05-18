package com.dsllt.oTravel_api.infra.adapter.place.out.persistence.mapper;


import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceEntityMapper {

    @Mapping(target = "name", source = "createPlaceRequestIn.name")
    @Mapping(target = "imageUrl", source = "createPlaceRequestIn.imageUrl")
    @Mapping(target = "description", source = "createPlaceRequestIn.description")
    @Mapping(target = "address", source = "createPlaceRequestIn.address")
    @Mapping(target = "city", source = "createPlaceRequestIn.city")
    @Mapping(target = "country", source = "createPlaceRequestIn.country")
    @Mapping(target = "latitude", source = "createPlaceRequestIn.latitude")
    @Mapping(target = "longitude", source = "createPlaceRequestIn.longitude")
    @Mapping(target = "slug", source = "createPlaceRequestIn.slug")
    @Mapping(target = "phone", source = "createPlaceRequestIn.phone")
    @Mapping(target = "category", source = "createPlaceRequestIn.category")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PlaceEntity toPlaceEntity(CreatePlaceRequestIn createPlaceRequestIn);
}
