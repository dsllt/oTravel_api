package com.dsllt.oTravel_api.dtos.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewDTO(
        @NotBlank(message = "A descrição deve ser definida.") String description,
        @NotNull(message = "Uma avaliação deve ser definida.") Double rating,
        @NotBlank(message = "O lugar deve ser definido.") String placeId,
        @NotBlank(message = "O usuário deve ser definido.") String userId
) {
}
