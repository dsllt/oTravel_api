package com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.mapper;

import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ScheduleEntityMapper {
    @Mapping(target = "weekDay", source = "createScheduleRequestIn.weekDay")
    @Mapping(target = "openAt", source = "createScheduleRequestIn.openAt")
    @Mapping(target = "closeAt", source = "createScheduleRequestIn.closeAt")
    @Mapping(target = "placeId", source = "placeId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ScheduleEntity toScheduleEntity(CreateScheduleRequestIn createScheduleRequestIn, UUID placeId);

    @Mapping(target = "weekDay", source = "updateScheduleRequestIn.weekDay")
    @Mapping(target = "openAt", source = "updateScheduleRequestIn.openAt")
    @Mapping(target = "closeAt", source = "updateScheduleRequestIn.closeAt")
    @Mapping(target = "id", source = "updateScheduleRequestIn.id")
    @Mapping(target = "placeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ScheduleEntity toScheduleEntity(UpdateScheduleRequestIn updateScheduleRequestIn);
}
