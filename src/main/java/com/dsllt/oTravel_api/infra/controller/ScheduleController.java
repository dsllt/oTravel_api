package com.dsllt.oTravel_api.infra.controller;

import com.dsllt.oTravel_api.core.usecase.ScheduleService;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> create(@Valid @RequestBody CreateScheduleDTO schedule){
        ScheduleDTO newSchedule = scheduleService.save(schedule);
        URI uri = URI.create("/schedule/" + newSchedule.id());
        return ResponseEntity.created(uri).body(newSchedule);
    }

    @GetMapping("/{placeUuid}")
    public ResponseEntity<ScheduleDTO[]> getByPlaceId(@Nonnull @PathVariable UUID placeUuid){
        ScheduleDTO[] schedules = scheduleService.getByPlaceId(placeUuid);
        return ResponseEntity.ok().body(schedules);
    }

    @PutMapping
    public ResponseEntity<ScheduleDTO> update(@Valid @RequestBody ScheduleDTO schedule){
        ScheduleDTO updatedSchedule = scheduleService.update(schedule);
        return ResponseEntity.ok().body(updatedSchedule);
    }
}
