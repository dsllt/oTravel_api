package com.dsllt.oTravel_api.domain.review.model;

import java.util.UUID;

public record ReviewFilter (
        UUID placeUuid,
        UUID userUuid
) {}
