package com.dsllt.oTravel_api.infra.adapter.review.in.web.model;

import lombok.Builder;

@Builder
public record ReviewResponse(
        String description,
        Double rating,
        String placeName,
        String userFirstName,
        String userLastName
) {}
