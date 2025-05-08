package com.dsllt.oTravel_api.domain.port.out;

import com.dsllt.oTravel_api.domain.model.schedule.Schedule;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface GetSchedulePort {
    List<Schedule> get(UUID placeUuid);
}
