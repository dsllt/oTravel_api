package com.dsllt.oTravel_api.usecase;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.ScheduleService;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleInfoDTO;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.ScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    PlaceRepository placeRepository;
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    @DisplayName("Should throw exception when schedule is already registered")
    public void save() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        ScheduleInfoDTO scheduleInfo1 = new ScheduleInfoDTO(WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now());
        ScheduleInfoDTO scheduleInfo2 = new ScheduleInfoDTO(WeekDay.MONDAY,
                OffsetTime.now(),
                OffsetTime.now());
        List<ScheduleInfoDTO> schedulesInfo = new ArrayList<>();
        schedulesInfo.add(scheduleInfo1);
        schedulesInfo.add(scheduleInfo2);
        CreateScheduleDTO createScheduleDTO = new CreateScheduleDTO(
                schedulesInfo,
                placeUUID.toString()
        );
        Mockito.when(scheduleRepository.existsByWeekDayAndPlaceId(Mockito.any(WeekDay.class), Mockito.any(UUID.class)))
                .thenReturn(true);
        Mockito.when(placeRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.ofNullable(place));
        // Act
        Throwable exception = catchException(() -> scheduleService.save(createScheduleDTO));
        // Assert
        assertThat(exception).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Should save a schedule")
    public void save2() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        ScheduleInfoDTO scheduleInfo1 = new ScheduleInfoDTO(WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now());
        ScheduleInfoDTO scheduleInfo2 = new ScheduleInfoDTO(WeekDay.MONDAY,
                OffsetTime.now(),
                OffsetTime.now());
        List<ScheduleInfoDTO> schedulesInfo = new ArrayList<>();
        schedulesInfo.add(scheduleInfo1);
        schedulesInfo.add(scheduleInfo2);
        CreateScheduleDTO createScheduleDTO = new CreateScheduleDTO(
                schedulesInfo,
                placeUUID.toString()
        );
        Schedule persistedSchedule1 = new Schedule(scheduleInfo1, place);
        Mockito.when(scheduleRepository.save(Mockito.any(Schedule.class)))
                .thenReturn(persistedSchedule1);
        Mockito.when(placeRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.ofNullable(place));
        // Act
        List<ScheduleDTO> savedSchedule = scheduleService.save(createScheduleDTO);
        // Assert
        Assertions.assertNotNull(savedSchedule);
        Assertions.assertEquals(createScheduleDTO.scheduleInfo().get(0).weekDay(), savedSchedule.get(0).weekDay());
        Assertions.assertEquals(createScheduleDTO.scheduleInfo().get(0).openAt(), savedSchedule.get(0).openAt());
        Assertions.assertEquals(createScheduleDTO.scheduleInfo().get(0).closeAt(), savedSchedule.get(0).closeAt());
    }

    @Test
    @DisplayName("Should throw exception when no data is found")
    public void get() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(false);
        // Act
        Throwable exception = catchException(() -> scheduleService.getByPlaceId(placeUUID));
        // Assert
        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Horários não encontrados para este local.");
    }

    @Test
    @DisplayName("Should retrieve all schedules for a place")
    public void get2() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        Schedule persistedScheduleSunday = new Schedule(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place,
                ZonedDateTime.now(),
                ZonedDateTime.now()) ;
        Schedule persistedScheduleMonday = new Schedule(
                1L,
                WeekDay.MONDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place,
                ZonedDateTime.now(),
                ZonedDateTime.now()) ;
        List<Schedule> persistedSchedules = new ArrayList<>();
        persistedSchedules.add(persistedScheduleMonday);
        persistedSchedules.add(persistedScheduleSunday);
        ScheduleDTO[] persistedSchedulesDTO = persistedSchedules.stream()
                        .map(ScheduleDTO::from)
                        .toArray(ScheduleDTO[]::new);
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
        Mockito.when(scheduleRepository.findByPlaceId(placeUUID))
                .thenReturn(persistedSchedules);
        // Act
        ScheduleDTO[] savedSchedules = scheduleService.getByPlaceId(placeUUID);
        // Assert
        Assertions.assertNotNull(savedSchedules);
        Assertions.assertEquals(savedSchedules.length, persistedSchedulesDTO.length);
        Assertions.assertArrayEquals(savedSchedules, persistedSchedulesDTO);
    }

    @Test
    @DisplayName("Should throw exception when no data is found")
    public void update() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        ScheduleDTO scheduleDTO = new ScheduleDTO(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                placeUUID) ;
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(false);
        Mockito.when(placeRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.ofNullable(place));
        // Act
        Throwable exception = catchException(() -> scheduleService.update(scheduleDTO));
        // Assert
        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Horários não encontrados para este local.");
    }

    @Test
    @DisplayName("Should update places schedule")
    public void update2() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        ScheduleDTO scheduleDTO = new ScheduleDTO(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                placeUUID) ;
        Schedule persistedSchedule = new Schedule(scheduleDTO, place);
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
        Mockito.when(scheduleRepository.save(Mockito.any(Schedule.class)))
                .thenReturn(persistedSchedule);
        Mockito.when(placeRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.ofNullable(place));
        // Act
        ScheduleDTO updatedSchedule = scheduleService.update(scheduleDTO);
        // Assert
        Assertions.assertNotNull(updatedSchedule);
        Assertions.assertEquals(updatedSchedule.id(),scheduleDTO.id());
        Assertions.assertEquals(updatedSchedule.weekDay(),scheduleDTO.weekDay());
    }
}