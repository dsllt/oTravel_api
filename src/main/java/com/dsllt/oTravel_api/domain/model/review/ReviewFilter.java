package com.dsllt.oTravel_api.core.entity.review;

import java.util.UUID;

public record ReviewFilter (
        UUID placeUuid,
        UUID userUuid
) {}
