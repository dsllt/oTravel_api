package com.dsllt.oTravel_api.domain.port.in;

import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;

import java.util.List;
import java.util.UUID;


public interface ScheduleUseCase {
    List<ScheduleResponseOut> getByPlaceId(UUID placeUuid);
    List<ScheduleResponseOut> update(List<UpdateScheduleRequestIn> updateScheduleRequestIn);
}
