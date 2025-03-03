package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleDTO save(CreateScheduleDTO scheduleDTO){
        if(scheduleRepository.existsByWeekDayAndPlaceId(scheduleDTO.weekDay(), scheduleDTO.place().getId())){
            String message = String.format("Horário %s já cadastrado para %s.", scheduleDTO.weekDay(), scheduleDTO.place().getName());
            throw new BusinessException(message);
        }
        Schedule schedule = new Schedule(scheduleDTO);
        Schedule persistedSchedule = scheduleRepository.save(schedule);
        return ScheduleDTO.from(persistedSchedule);
    }

    public ScheduleDTO[] getByPlaceId(UUID placeId){
        if(scheduleRepository.existsByPlaceId(placeId)){
            throw new ObjectNotFoundException("Horários não encontrados para este local.");
        }
        Schedule[] schedules = scheduleRepository.findByPlaceId(placeId);
      return Arrays.stream(schedules)
              .map(ScheduleDTO::from)
              .toArray(ScheduleDTO[]::new);
    }

    public ScheduleDTO update(ScheduleDTO scheduleDTO){
        if(!scheduleRepository.existsByPlaceId(scheduleDTO.place().getId())){
            throw new ObjectNotFoundException("Horários não encontrados para este local.");
        }
        Schedule schedule = new Schedule(scheduleDTO);
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ScheduleDTO.from(updatedSchedule);
    }
}
