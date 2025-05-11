package com.dsllt.oTravel_api.domain.schedule.port.out;

import com.dsllt.oTravel_api.domain.schedule.model.Schedule;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface CreateSchedulePort {

    List<Schedule> create(List<CreateScheduleRequestIn> createScheduleRequestIn, UUID placeUuid);
}
