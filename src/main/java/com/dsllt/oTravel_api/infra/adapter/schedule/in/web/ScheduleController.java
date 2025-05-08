package com.dsllt.oTravel_api.infra.adapter.schedule.in.web;

import com.dsllt.oTravel_api.domain.port.in.CreateScheduleUseCase;
import com.dsllt.oTravel_api.domain.port.in.ScheduleUseCase;
import com.dsllt.oTravel_api.domain.service.ScheduleService;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedule")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleUseCase scheduleUseCase;
    private final CreateScheduleUseCase createScheduleUseCase;

    @PostMapping("/{placeUuid}")
    public ResponseEntity<List<ScheduleResponseOut>> create(@Valid @RequestBody List<CreateScheduleRequestIn> createScheduleRequestIn, @Nonnull @PathVariable UUID placeUuid){

        List<ScheduleResponseOut> newSchedule = createScheduleUseCase.create(createScheduleRequestIn, placeUuid);
        URI uri = URI.create("/schedule/" + placeUuid);
        return ResponseEntity.created(uri).body(newSchedule);
    }

    @GetMapping("/{placeUuid}")
    public ResponseEntity<List<ScheduleResponseOut>> getByPlaceId(@Nonnull @PathVariable UUID placeUuid){
        List<ScheduleResponseOut> schedules = scheduleUseCase.getByPlaceId(placeUuid);
        return ResponseEntity.ok().body(schedules);
    }

    @PutMapping
    public ResponseEntity<List<ScheduleResponseOut>> update(@Valid @RequestBody List<UpdateScheduleRequestIn> schedule){
        List<ScheduleResponseOut> updatedSchedule = scheduleUseCase.update(schedule);
        return ResponseEntity.ok().body(updatedSchedule);
    }
}
