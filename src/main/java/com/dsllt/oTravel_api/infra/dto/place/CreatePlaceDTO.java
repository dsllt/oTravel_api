package com.dsllt.oTravel_api.infra.dto.place;

import com.dsllt.oTravel_api.infra.enums.PlaceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePlaceDTO(
        @NotBlank(message = "O nome deve ser definido.") String name,
        String imageUrl,
        String description,
        @NotBlank(message = "O endereço deve ser definido.") String address,
        @NotBlank(message = "A cidade deve ser definida.") String city,
        @NotBlank(message = "O país deve ser definido.") String country,
        @NotNull(message = "A latitude deve ser definida.") Double latitude,
        @NotNull(message = "A longitude deve ser definida.") Double longitude,
        @NotBlank(message = "O slug deve ser definido.") String slug,
        String phone,
        @NotNull(message = "A categoria deve ser definida.") List<PlaceCategory> category) {
}
