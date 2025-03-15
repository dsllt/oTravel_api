package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private PlaceRepository placeRepository;

    public List<ScheduleDTO> save(CreateScheduleDTO scheduleDTO){
        UUID placeUUID = UUID.fromString(scheduleDTO.placeId());
        Place place = placeRepository.findById(placeUUID).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado"));
        List<Schedule> savedScheduleList = new ArrayList<>();
        scheduleDTO.scheduleInfo().forEach(schedule -> {
            if(scheduleRepository.existsByWeekDayAndPlaceId(schedule.weekDay(), placeUUID)){
                String message = String.format("Horário %s já cadastrado para %s.", schedule.weekDay(), scheduleDTO.placeId());
                throw new BusinessException(message);
            }
            Schedule newSchedule = new Schedule(schedule, place);
            Schedule persistedSchedule = scheduleRepository.save(newSchedule);
            savedScheduleList.add(persistedSchedule);
        });

        return savedScheduleList.stream().map(ScheduleDTO::from).toList();
    }

    public ScheduleDTO[] getByPlaceId(UUID placeId){
        if(!scheduleRepository.existsByPlaceId(placeId)){
            throw new ObjectNotFoundException("Horários não encontrados para este local.");
        }
        List<Schedule> schedules = scheduleRepository.findByPlaceId(placeId);
        return schedules.stream()
              .map(ScheduleDTO::from)
              .toArray(ScheduleDTO[]::new);
    }

    public ScheduleDTO update(ScheduleDTO scheduleDTO){
        Place place = placeRepository.findById(scheduleDTO.placeId()).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado"));
        if(!scheduleRepository.existsByPlaceId(scheduleDTO.placeId())){
            throw new ObjectNotFoundException("Horários não encontrados para este local.");
        }
        Schedule schedule = new Schedule(scheduleDTO, place);
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ScheduleDTO.from(updatedSchedule);
    }
}
