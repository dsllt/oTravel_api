package com.dsllt.oTravel_api.domain.schedule.service;

import com.dsllt.oTravel_api.domain.schedule.model.Schedule;
import com.dsllt.oTravel_api.domain.schedule.port.in.CreateScheduleUseCase;
import com.dsllt.oTravel_api.domain.schedule.port.in.ScheduleUseCase;
import com.dsllt.oTravel_api.domain.schedule.port.out.CreateSchedulePort;
import com.dsllt.oTravel_api.domain.schedule.port.out.GetSchedulePort;
import com.dsllt.oTravel_api.domain.schedule.port.out.UpdateSchedulePort;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.mapper.ScheduleResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ScheduleService implements ScheduleUseCase, CreateScheduleUseCase {

    private CreateSchedulePort createSchedulePort;
    private GetSchedulePort getSchedulePort;
    private UpdateSchedulePort updateSchedulePort;
    private ScheduleResponseOutMapper scheduleResponseOutMapper;

    @Override
    public List<ScheduleResponseOut> create(List<CreateScheduleRequestIn> createScheduleRequestIn, UUID placeUuid){
        List<Schedule> savedScheduleList = createSchedulePort.create(createScheduleRequestIn, placeUuid);

        return savedScheduleList.stream().map(scheduleResponseOutMapper::toScheduleResponseOut).toList();
    }

    public List<ScheduleResponseOut> getByPlaceId(UUID placeId){
        List<Schedule> schedules = getSchedulePort.get(placeId);

        return schedules.stream()
                .map(scheduleResponseOutMapper::toScheduleResponseOut)
                .toList();
    }

    public List<ScheduleResponseOut> update(List<UpdateScheduleRequestIn> updateScheduleRequestIn){
        var updatedSchedule = updateSchedulePort.update(updateScheduleRequestIn);

        return updatedSchedule.stream()
                .map(scheduleResponseOutMapper::toScheduleResponseOut).toList();
    }
}
