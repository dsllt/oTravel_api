package com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.schedule.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
    boolean existsByWeekDayAndPlaceId(WeekDay weekDay, UUID placeId);
    boolean existsByPlaceId(UUID placeId);
    List<ScheduleEntity> findByPlaceId(UUID placeId);
}
