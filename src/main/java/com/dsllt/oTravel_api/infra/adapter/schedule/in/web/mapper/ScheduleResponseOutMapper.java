package com.dsllt.oTravel_api.infra.adapter.schedule.in.web.mapper;

import com.dsllt.oTravel_api.domain.model.schedule.Schedule;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseOutMapper {

    @Mapping(target = "weekDay", source = "schedule.weekDay")
    @Mapping(target = "openAt", source = "schedule.openAt")
    @Mapping(target = "closeAt", source = "schedule.closeAt")
    ScheduleResponseOut toScheduleResponseOut(Schedule schedule);

}
