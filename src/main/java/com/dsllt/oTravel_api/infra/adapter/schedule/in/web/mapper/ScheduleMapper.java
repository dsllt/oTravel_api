package com.dsllt.oTravel_api.infra.adapter.schedule.in.web.mapper;

import com.dsllt.oTravel_api.domain.schedule.model.Schedule;
import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "id", source = "scheduleEntity.id")
    @Mapping(target = "weekDay", source = "scheduleEntity.weekDay")
    @Mapping(target = "openAt", source = "scheduleEntity.openAt")
    @Mapping(target = "closeAt", source = "scheduleEntity.closeAt")
    @Mapping(target = "placeId", source = "scheduleEntity.placeId")
    @Mapping(target = "createdAt", source = "scheduleEntity.createdAt")
    @Mapping(target = "updatedAt", source = "scheduleEntity.updatedAt")
    Schedule toSchedule(ScheduleEntity scheduleEntity);
}
