package com.dsllt.oTravel_api.domain.schedule.port.in;

import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface CreateScheduleUseCase {

    List<ScheduleResponseOut> create(List<CreateScheduleRequestIn> createScheduleRequestIn, UUID placeUuid);
}
