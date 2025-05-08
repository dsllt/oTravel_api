package com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence;

import com.dsllt.oTravel_api.domain.model.schedule.Schedule;
import com.dsllt.oTravel_api.domain.port.out.CreateSchedulePort;
import com.dsllt.oTravel_api.domain.port.out.GetSchedulePort;
import com.dsllt.oTravel_api.domain.port.out.UpdateSchedulePort;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.mapper.ScheduleMapper;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa.ScheduleEntity;
import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa.ScheduleJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.mapper.ScheduleEntityMapper;
import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ScheduleRepository implements CreateSchedulePort, GetSchedulePort, UpdateSchedulePort {

    private final ScheduleJpaRepository scheduleJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleEntityMapper scheduleEntityMapper;

    @Override
    public List<Schedule> create(List<CreateScheduleRequestIn> createScheduleRequestIn, UUID placeUuid) {
        placeJpaRepository.findById(placeUuid).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado"));
        List<ScheduleEntity> savedScheduleList = new ArrayList<>();
        createScheduleRequestIn.forEach(schedule -> {
            if(scheduleJpaRepository.existsByWeekDayAndPlaceId(schedule.weekDay(), placeUuid)){
                String message = String.format("Horário %s já cadastrado para %s.", schedule.weekDay(), placeUuid);
                throw new BusinessException(message);
            }
            var newSchedule = scheduleEntityMapper.toScheduleEntity(schedule, placeUuid);
            ScheduleEntity persistedSchedule = scheduleJpaRepository.save(newSchedule);

            savedScheduleList.add(persistedSchedule);
        });

        return savedScheduleList.stream().map(scheduleMapper::toSchedule).toList();
    }

    @Override
    public List<Schedule> get(UUID placeUuid) {
        if(!scheduleJpaRepository.existsByPlaceId(placeUuid)){
            throw new ObjectNotFoundException("Horários não encontrados para este local.");
        }
        List<ScheduleEntity> retrievedSchedules = scheduleJpaRepository.findByPlaceId(placeUuid);
        return retrievedSchedules.stream().map(scheduleMapper::toSchedule).toList();
    }

    @Override
    public List<Schedule> update(List<UpdateScheduleRequestIn> updateScheduleRequestIn) {
        updateScheduleRequestIn.forEach(schedule -> {
            System.out.println("VALO" + scheduleJpaRepository.existsById(schedule.id()));
            if(!scheduleJpaRepository.existsById(schedule.id())){
                throw new ObjectNotFoundException("Horários não encontrados.");
            }
        });

        List<ScheduleEntity> schedule = updateScheduleRequestIn.stream()
                .map(scheduleEntityMapper::toScheduleEntity).toList();
        List<ScheduleEntity> updatedSchedules = scheduleJpaRepository.saveAll(schedule);

        return updatedSchedules.stream().map(scheduleMapper::toSchedule).toList();
    }
}
