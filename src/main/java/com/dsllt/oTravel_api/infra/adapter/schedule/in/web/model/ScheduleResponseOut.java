package com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model;

import com.dsllt.oTravel_api.domain.model.schedule.WeekDay;
import lombok.Builder;

import java.time.OffsetTime;

@Builder(toBuilder = true)
public record ScheduleResponseOut(
        WeekDay weekDay,
        OffsetTime openAt,
        OffsetTime closeAt
) {
}
