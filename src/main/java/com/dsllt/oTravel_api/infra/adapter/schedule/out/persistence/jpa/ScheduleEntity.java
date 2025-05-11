package com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.schedule.model.WeekDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "schedules")
public class ScheduleEntity {

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
    @Column(name = "place_id")
    private UUID placeId;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
