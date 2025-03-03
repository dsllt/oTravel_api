package com.dsllt.oTravel_api.usecase;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.ScheduleService;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
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
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    @DisplayName("Should throw exception when schedule is already registered")
    public void save() {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        CreateScheduleDTO createScheduleDTO = new CreateScheduleDTO(
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place
        );
        Mockito.when(scheduleRepository.existsByWeekDayAndPlaceId(Mockito.any(WeekDay.class), Mockito.any(UUID.class)))
                .thenReturn(true);
        // Act
        Throwable exception = catchException(() -> scheduleService.save(createScheduleDTO));
        // Assert
        assertThat(exception).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Should save a schedule")
    public void save2() {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        CreateScheduleDTO createScheduleDTO = new CreateScheduleDTO(
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place
        );
        Schedule persistedSchedule = new Schedule(createScheduleDTO);
        Mockito.when(scheduleRepository.save(Mockito.any(Schedule.class)))
                .thenReturn(persistedSchedule);
        // Act
        ScheduleDTO savedSchedule = scheduleService.save(createScheduleDTO);
        // Assert
        Assertions.assertNotNull(savedSchedule);
        Assertions.assertEquals(createScheduleDTO.weekDay(), savedSchedule.weekDay());
        Assertions.assertEquals(createScheduleDTO.openAt(), savedSchedule.openAt());
        Assertions.assertEquals(createScheduleDTO.closeAt(), savedSchedule.closeAt());
    }

    @Test
    @DisplayName("Should throw exception when no data is found")
    public void get() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
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
        Schedule[] persistedSchedules = {persistedScheduleMonday, persistedScheduleSunday};
        ScheduleDTO[] persistedSchedulesDTO = Arrays.stream(persistedSchedules)
                        .map(ScheduleDTO::from)
                        .toArray(ScheduleDTO[]::new);
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
                place) ;
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(false);
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
                place) ;
        Schedule persistedSchedule = new Schedule(scheduleDTO);
        Mockito.when(scheduleRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
        Mockito.when(scheduleRepository.save(Mockito.any(Schedule.class)))
                .thenReturn(persistedSchedule);
        // Act
        ScheduleDTO updatedSchedule = scheduleService.update(scheduleDTO);
        // Assert
        Assertions.assertNotNull(updatedSchedule);
        Assertions.assertEquals(updatedSchedule.id(),scheduleDTO.id());
        Assertions.assertEquals(updatedSchedule.weekDay(),scheduleDTO.weekDay());
    }
}