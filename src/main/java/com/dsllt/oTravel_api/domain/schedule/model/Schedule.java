package com.dsllt.oTravel_api.domain.schedule.model;

import lombok.Builder;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Schedule(
        Long id,
        WeekDay weekDay,
        OffsetTime openAt,
        OffsetTime closeAt,
        UUID placeId,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
}
