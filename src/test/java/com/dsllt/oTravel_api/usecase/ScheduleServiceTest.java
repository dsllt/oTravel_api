//package com.dsllt.oTravel_api.usecase;
//
//import com.dsllt.oTravel_api.domain.place.model.Place;
//import com.dsllt.oTravel_api.domain.schedule.model.Schedule;
//import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
//import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
//import com.dsllt.oTravel_api.domain.schedule.service.ScheduleService;
//import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
//import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
//import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleInfoDTO;
//import com.dsllt.oTravel_api.domain.schedule.model.WeekDay;
//import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
//import com.dsllt.oTravel_api.infra.adapter.schedule.out.persistence.jpa.ScheduleJpaRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.OffsetTime;
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchException;
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class ScheduleServiceTest {
//
//    @Mock
//    ScheduleJpaRepository scheduleJpaRepository;
//    @Mock
//    PlaceJpaRepository placeJpaRepository;
//    @InjectMocks
//    ScheduleService scheduleService;
//
//    @Test
//    @DisplayName("Should throw exception when schedule is already registered")
//    public void save() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        ScheduleInfoDTO scheduleInfo1 = new ScheduleInfoDTO(WeekDay.SUNDAY,
//                OffsetTime.now(),
//                OffsetTime.now());
//        ScheduleInfoDTO scheduleInfo2 = new ScheduleInfoDTO(WeekDay.MONDAY,
//                OffsetTime.now(),
//                OffsetTime.now());
//        List<ScheduleInfoDTO> schedulesInfo = new ArrayList<>();
//        schedulesInfo.add(scheduleInfo1);
//        schedulesInfo.add(scheduleInfo2);
//        CreateScheduleRequestIn createScheduleRequestIn = new CreateScheduleRequestIn(
//                schedulesInfo,
//                placeUUID.toString()
//        );
//        Mockito.when(scheduleJpaRepository.existsByWeekDayAndPlaceId(Mockito.any(WeekDay.class), Mockito.any(UUID.class)))
//                .thenReturn(true);
//        Mockito.when(placeJpaRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.ofNullable(place));
//        // Act
//        Throwable exception = catchException(() -> scheduleService.save(createScheduleRequestIn));
//        // Assert
//        assertThat(exception).isInstanceOf(BusinessException.class);
//    }
//
//    @Test
//    @DisplayName("Should save a schedule")
//    public void save2() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        ScheduleInfoDTO scheduleInfo1 = new ScheduleInfoDTO(WeekDay.SUNDAY,
//                OffsetTime.now(),
//                OffsetTime.now());
//        ScheduleInfoDTO scheduleInfo2 = new ScheduleInfoDTO(WeekDay.MONDAY,
//                OffsetTime.now(),
//                OffsetTime.now());
//        List<ScheduleInfoDTO> schedulesInfo = new ArrayList<>();
//        schedulesInfo.add(scheduleInfo1);
//        schedulesInfo.add(scheduleInfo2);
//        CreateScheduleRequestIn createScheduleRequestIn = new CreateScheduleRequestIn(
//                schedulesInfo,
//                placeUUID.toString()
//        );
//        Schedule persistedSchedule1 = new Schedule(scheduleInfo1, place);
//        Mockito.when(scheduleJpaRepository.save(Mockito.any(Schedule.class)))
//                .thenReturn(persistedSchedule1);
//        Mockito.when(placeJpaRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.ofNullable(place));
//        // Act
//        List<UpdateScheduleRequestIn> savedSchedule = scheduleService.save(createScheduleRequestIn);
//        // Assert
//        Assertions.assertNotNull(savedSchedule);
//        Assertions.assertEquals(createScheduleRequestIn.scheduleInfo().get(0).weekDay(), savedSchedule.get(0).weekDay());
//        Assertions.assertEquals(createScheduleRequestIn.scheduleInfo().get(0).openAt(), savedSchedule.get(0).openAt());
//        Assertions.assertEquals(createScheduleRequestIn.scheduleInfo().get(0).closeAt(), savedSchedule.get(0).closeAt());
//    }
//
//    @Test
//    @DisplayName("Should throw exception when no data is found")
//    public void get() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Mockito.when(scheduleJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(false);
//        // Act
//        Throwable exception = catchException(() -> scheduleService.getByPlaceId(placeUUID));
//        // Assert
//        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
//                .hasMessage("Horários não encontrados para este local.");
//    }
//
//    @Test
//    @DisplayName("Should retrieve all schedules for a place")
//    public void get2() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        Schedule persistedScheduleSunday = new Schedule(
//                1L,
//                WeekDay.SUNDAY,
//                OffsetTime.now(),
//                OffsetTime.now(),
//                place,
//                ZonedDateTime.now(),
//                ZonedDateTime.now()) ;
//        Schedule persistedScheduleMonday = new Schedule(
//                1L,
//                WeekDay.MONDAY,
//                OffsetTime.now(),
//                OffsetTime.now(),
//                place,
//                ZonedDateTime.now(),
//                ZonedDateTime.now()) ;
//        List<Schedule> persistedSchedules = new ArrayList<>();
//        persistedSchedules.add(persistedScheduleMonday);
//        persistedSchedules.add(persistedScheduleSunday);
//        UpdateScheduleRequestIn[] persistedSchedulesDTO = persistedSchedules.stream()
//                        .map(UpdateScheduleRequestIn::from)
//                        .toArray(UpdateScheduleRequestIn[]::new);
//        Mockito.when(scheduleJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(true);
//        Mockito.when(scheduleJpaRepository.findByPlaceId(placeUUID))
//                .thenReturn(persistedSchedules);
//        // Act
//        UpdateScheduleRequestIn[] savedSchedules = scheduleService.getByPlaceId(placeUUID);
//        // Assert
//        Assertions.assertNotNull(savedSchedules);
//        Assertions.assertEquals(savedSchedules.length, persistedSchedulesDTO.length);
//        Assertions.assertArrayEquals(savedSchedules, persistedSchedulesDTO);
//    }
//
//    @Test
//    @DisplayName("Should throw exception when no data is found")
//    public void update() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        UpdateScheduleRequestIn updateScheduleRequestIn = new UpdateScheduleRequestIn(
//                1L,
//                WeekDay.SUNDAY,
//                OffsetTime.now(),
//                OffsetTime.now(),
//                placeUUID) ;
//        Mockito.when(scheduleJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(false);
//        Mockito.when(placeJpaRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.ofNullable(place));
//        // Act
//        Throwable exception = catchException(() -> scheduleService.update(updateScheduleRequestIn));
//        // Assert
//        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
//                .hasMessage("Horários não encontrados para este local.");
//    }
//
//    @Test
//    @DisplayName("Should update places schedule")
//    public void update2() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        UpdateScheduleRequestIn updateScheduleRequestIn = new UpdateScheduleRequestIn(
//                1L,
//                WeekDay.SUNDAY,
//                OffsetTime.now(),
//                OffsetTime.now(),
//                placeUUID) ;
//        Schedule persistedSchedule = new Schedule(updateScheduleRequestIn, place);
//        Mockito.when(scheduleJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(true);
//        Mockito.when(scheduleJpaRepository.save(Mockito.any(Schedule.class)))
//                .thenReturn(persistedSchedule);
//        Mockito.when(placeJpaRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.ofNullable(place));
//        // Act
//        UpdateScheduleRequestIn updatedSchedule = scheduleService.update(updateScheduleRequestIn);
//        // Assert
//        Assertions.assertNotNull(updatedSchedule);
//        Assertions.assertEquals(updatedSchedule.id(), updateScheduleRequestIn.id());
//        Assertions.assertEquals(updatedSchedule.weekDay(), updateScheduleRequestIn.weekDay());
//    }
//}