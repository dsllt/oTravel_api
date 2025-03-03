package com.dsllt.oTravel_api.infra.repository;

import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public boolean existsByWeekDayAndPlaceId(WeekDay weekDay, UUID placeId);
    public boolean existsByPlaceId(UUID placeId);
    public Schedule[] findByPlaceId(UUID placeId);
}
