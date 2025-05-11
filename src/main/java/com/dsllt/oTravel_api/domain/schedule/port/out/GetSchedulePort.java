package com.dsllt.oTravel_api.domain.schedule.port.out;

import com.dsllt.oTravel_api.domain.schedule.model.Schedule;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface GetSchedulePort {
    List<Schedule> get(UUID placeUuid);
}
