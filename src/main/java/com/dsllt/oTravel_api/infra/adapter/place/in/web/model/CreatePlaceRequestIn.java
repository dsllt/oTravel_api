package com.dsllt.oTravel_api.infra.adapter.place.in.web.model;

import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record CreatePlaceRequestIn(
        @NotBlank(message = "O nome deve ser definido.")
        String name,
        String imageUrl,
        String description,
        @NotBlank(message = "O endereço deve ser definido.")
        String address,
        @NotBlank(message = "A cidade deve ser definida.")
        String city,
        @NotBlank(message = "O país deve ser definido.")
        String country,
        @NotNull(message = "A latitude deve ser definida.")
        Double latitude,
        @NotNull(message = "A longitude deve ser definida.")
        Double longitude,
        @NotBlank(message = "O slug deve ser definido.")
        String slug,
        String phone,
        @NotNull(message = "A categoria deve ser definida.")
        List<PlaceCategory> category
) {}
