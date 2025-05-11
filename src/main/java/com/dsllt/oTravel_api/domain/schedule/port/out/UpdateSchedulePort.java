package com.dsllt.oTravel_api.domain.schedule.port.out;

import com.dsllt.oTravel_api.domain.schedule.model.Schedule;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;

import java.util.List;

@FunctionalInterface
public interface UpdateSchedulePort {
    List<Schedule> update(List<UpdateScheduleRequestIn> updateScheduleRequestIn);
}
