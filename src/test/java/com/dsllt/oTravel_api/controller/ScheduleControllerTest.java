package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.schedule.Schedule;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.ScheduleService;
import com.dsllt.oTravel_api.infra.dto.schedule.CreateScheduleDTO;
import com.dsllt.oTravel_api.infra.dto.schedule.ScheduleDTO;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("Should return status code 400 when trying to create schedule with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        CreateScheduleDTO newSchedule = new CreateScheduleDTO(
                null,
                null,
                null,
                null);
        String requestBody = objectMapper.writeValueAsString(newSchedule);
        // Act and assert
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Should return status code 201 when creating a schedule")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        Place place = Place.builder().build();
        CreateScheduleDTO createScheduleDTO = new CreateScheduleDTO(
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        Schedule newSchedule = new Schedule(createScheduleDTO);
        ScheduleDTO newScheduleDTO = ScheduleDTO.from(newSchedule);
        Mockito.when(scheduleService.save(Mockito.any(CreateScheduleDTO.class))).thenReturn(newScheduleDTO);
        String requestBody = objectMapper.writeValueAsString(newScheduleDTO);
        // Act and assert
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newSchedule.getId()))
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).save(Mockito.any(CreateScheduleDTO.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create a schedule for a weekday and place that already exist")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate3() throws Exception {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        CreateScheduleDTO newScheduleDTO = new CreateScheduleDTO(
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        Mockito.when(scheduleService.save(Mockito.any(CreateScheduleDTO.class)))
                .thenThrow(BusinessException.class);
        String requestBody = objectMapper.writeValueAsString(newScheduleDTO);
        // Act and assert
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).save(Mockito.any(CreateScheduleDTO.class));
    }

    @Test
    @DisplayName("Should list all week days with schedule hours saved")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        Place place = Place.builder().id(placeId).build();
        ScheduleDTO sundaySchedule = new ScheduleDTO(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        ScheduleDTO mondaySchedule = new ScheduleDTO(
                2L,
                WeekDay.MONDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        ScheduleDTO[] schedules = {sundaySchedule,mondaySchedule};
        String requestResponse = objectMapper.writeValueAsString(schedules);
        Mockito.when(scheduleService.getByPlaceId(placeId))
                .thenReturn(schedules);
        // Act and assert
        mockMvc.perform(get("/api/v1/schedule/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestResponse))
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).getByPlaceId(placeId);
    }

    @Test
    @DisplayName("Should throw an exception when there is no schedule saved for the place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet2() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        Mockito.when(scheduleService.getByPlaceId(placeId))
                .thenThrow(ObjectNotFoundException.class);
        // Act and assert
        mockMvc.perform(get("/api/v1/schedule/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).getByPlaceId(placeId);
    }

    @Test
    @DisplayName("Should throw an exception when trying to update unregistered place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update() throws Exception {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        ScheduleDTO scheduleDTO = new ScheduleDTO(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        String requestBody = objectMapper.writeValueAsString(scheduleDTO);
        Mockito.when(scheduleService.update(Mockito.any(ScheduleDTO.class)))
                .thenThrow(ObjectNotFoundException.class);
        // Act and assert
        mockMvc.perform(put("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).update(Mockito.any(ScheduleDTO.class));
    }

    @Test
    @DisplayName("Should allow to update a schedule")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update2() throws Exception {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        ScheduleDTO scheduleDTO = new ScheduleDTO(
                1L,
                WeekDay.SUNDAY,
                OffsetTime.now(),
                OffsetTime.now(),
                place);
        String requestBody = objectMapper.writeValueAsString(scheduleDTO);
        Mockito.when(scheduleService.update(scheduleDTO))
                .thenReturn(scheduleDTO);
        // Act and assert
        mockMvc.perform(put("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
        Mockito.verify(scheduleService, Mockito.times(1)).update(Mockito.any(ScheduleDTO.class));
    }

}