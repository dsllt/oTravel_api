package com.dsllt.oTravel_api.core.entity.schedule;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.OffsetTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private WeekDay weekDay;
    @Column(name = "open_at")
    private OffsetTime openAt;
    @Column(name = "close_at")
    private OffsetTime closeAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Schedule(CreateScheduleDTO scheduleDTO){
        this.weekDay = scheduleDTO.weekDay();
        this.openAt = scheduleDTO.openAt();
        this.closeAt = scheduleDTO.closeAt();
        this.place= scheduleDTO.place();
    }

    public Schedule(ScheduleDTO scheduleDTO){
        this.id = scheduleDTO.id();
        this.weekDay = scheduleDTO.weekDay();
        this.openAt = scheduleDTO.openAt();
        this.closeAt = scheduleDTO.closeAt();
        this.place= scheduleDTO.place();
    }


}
