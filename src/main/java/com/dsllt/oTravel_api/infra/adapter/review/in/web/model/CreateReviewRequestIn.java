package com.dsllt.oTravel_api.infra.adapter.review.in.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateReviewRequestIn(
        @NotBlank(message = "A descrição deve ser definida.") String description,
        @NotNull(message = "Uma avaliação deve ser definida.") Double rating,
        @NotBlank(message = "O lugar deve ser definido.") String placeId,
        @NotBlank(message = "O usuário deve ser definido.") String userId
) {
}
