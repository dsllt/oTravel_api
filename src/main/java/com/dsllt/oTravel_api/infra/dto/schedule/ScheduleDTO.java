package com.dsllt.oTravel_api.infra.dto.schedule;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.infra.enums.WeekDay;

import java.time.OffsetTime;

public record ScheduleDTO(
        Long id,
        WeekDay weekDay,
        OffsetTime openAt,
        OffsetTime closeAt,
        Place place
) {
    public static ScheduleDTO from(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getId(),
                schedule.getWeekDay(),
                schedule.getOpenAt(),
                schedule.getCloseAt(),
                schedule.getPlace()
        );
    }
}
