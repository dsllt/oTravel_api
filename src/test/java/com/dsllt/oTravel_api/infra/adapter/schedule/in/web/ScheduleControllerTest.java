package com.dsllt.oTravel_api.infra.adapter.schedule.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.model.schedule.WeekDay;
import com.dsllt.oTravel_api.domain.service.ScheduleService;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.CreateScheduleRequestIn;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.ScheduleResponseOut;
import com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model.UpdateScheduleRequestIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM schedules");
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("""
                    INSERT INTO places (
                        id, name, image_url, description, address,
                        city, country, latitude, longitude, slug,
                        phone, category, rating, created_at
                    ) VALUES (
                        '550e8400-e29b-41d4-a716-446655440000',
                        'Test Place',
                        'https://example.com/image.jpg',
                        'A beautiful test place',
                        '123 Test St',
                        'Testville',
                        'Testland',
                        10.123456,
                        20.654321,
                        'test-place',
                        '+1234567890',
                        'Museum',
                        4.5,
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    @Test
    @DisplayName("Should return status code 400 when trying to create schedule with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        var placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        CreateScheduleRequestIn createScheduleRequestIn = new CreateScheduleRequestIn(null, null, null);
        // Act and assert
        performCreateScheduleRequest(placeUuid, createScheduleRequestIn)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Should return status code 201 when creating a schedule")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var createScheduleRequestIn = createScheduleRequestInListWithTwoElements();
        var scheduleResponseOut = createScheduleResponseOutListWithTwoElements();
        var responseBody = objectMapper.writeValueAsString(scheduleResponseOut);
        // Act and assert
        performCreateScheduleRequest(placeUuid, createScheduleRequestIn)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/schedule/" + placeUuid))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Test
    @DisplayName("Should throw exception when trying to create a schedule for a weekday and place that already exist")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate3() throws Exception {
        // Arrange
        var placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var createScheduleRequestIn = createScheduleRequestInListWithOneElement();
        // Act and assert
        performCreateScheduleRequest(placeUuid, createScheduleRequestIn);
        performCreateScheduleRequest(placeUuid, createScheduleRequestIn)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("Should list all week days with schedule hours saved")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet() throws Exception {
        // Arrange
        var placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var createScheduleRequestIn = createScheduleRequestInListWithTwoElements();
        var scheduleResponseOut = createScheduleResponseOutListWithTwoElements();
        var responseBody = objectMapper.writeValueAsString(scheduleResponseOut);
        performCreateScheduleRequest(placeUuid, createScheduleRequestIn);

        // Act and assert
        mockMvc.perform(get("/api/v1/schedule/" + placeUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Test
    @DisplayName("Should throw an exception when there is no schedule saved for the place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet2() throws Exception {
        // Arrange
        var placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        // Act and assert
        mockMvc.perform(get("/api/v1/schedule/" + placeUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should throw an exception when trying to update unregistered place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update() throws Exception {
        // Arrange
        var updateScheduleRequestIn = createUpdateScheduleRequestInWithOneElement();
        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestIn);

        // Act and assert
        mockMvc.perform(put("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("Should allow to update a schedule")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update2() throws Exception {
        // Arrange
        UUID placeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        OffsetTime openAt = OffsetTime.parse("20:15:16-03:00");
        OffsetTime closeAt = OffsetTime.parse("22:00:00-03:00");

        jdbcTemplate.update("""
        INSERT INTO schedules (id, week_day, open_at, close_at, place_id)
        VALUES (?, ?, ?, ?, ?)
    """, 1L, "SUNDAY", openAt, closeAt, placeId);

        List<UpdateScheduleRequestIn> updateScheduleRequestIn = List.of(
                UpdateScheduleRequestIn.builder()
                        .id(1L)
                        .weekDay(WeekDay.SUNDAY)
                        .openAt(openAt)
                        .closeAt(closeAt)
                        .build()
        );

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestIn);

        // Act and assert
        mockMvc.perform(put("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private ResultActions performCreateScheduleRequest(UUID placeUuid, Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post("/api/v1/schedule/" + placeUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private List<CreateScheduleRequestIn> createScheduleRequestInListWithOneElement() {
        var time = OffsetTime.now().truncatedTo(ChronoUnit.SECONDS);
        return List.of(
                CreateScheduleRequestIn.builder()
                        .weekDay(WeekDay.SUNDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build()
        );
    }

    private List<CreateScheduleRequestIn> createScheduleRequestInListWithTwoElements() {
        var time = OffsetTime.now().truncatedTo(ChronoUnit.SECONDS);
        return List.of(
                CreateScheduleRequestIn.builder()
                        .weekDay(WeekDay.SUNDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build(),
                CreateScheduleRequestIn.builder()
                        .weekDay(WeekDay.MONDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build()
        );
    }

    private List<ScheduleResponseOut> createScheduleResponseOutListWithTwoElements() {
        var time = OffsetTime.now().truncatedTo(ChronoUnit.SECONDS);
        return List.of(
                ScheduleResponseOut.builder()
                        .weekDay(WeekDay.SUNDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build(),
                ScheduleResponseOut.builder()
                        .weekDay(WeekDay.MONDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build()
        );
    }

    private List<UpdateScheduleRequestIn> createUpdateScheduleRequestInWithOneElement() {
        var time = OffsetTime.now().truncatedTo(ChronoUnit.SECONDS);
        return List.of(
                UpdateScheduleRequestIn.builder()
                        .id(1L)
                        .weekDay(WeekDay.SUNDAY)
                        .openAt(time)
                        .closeAt(time)
                        .build()
        );
    }


}