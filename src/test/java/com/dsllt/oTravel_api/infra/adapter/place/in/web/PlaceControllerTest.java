package com.dsllt.oTravel_api.infra.adapter.place.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.UUID;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
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
                        'COFFEE',
                        4.5,
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM schedules");
    }

    @Test
    @DisplayName("should throw exception when trying to register a place with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/api/v1/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should create place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var createPlaceRequestIn = CreatePlaceRequestIn.builder()
                .name("Lugar novo")
                .address("Rua XX")
                .city("Porto Alegre")
                .country("Brazil")
                .latitude(30.0319164)
                .longitude(-51.210757)
                .slug("lugar-novo")
                .category(of(PlaceCategory.COFFEE))
                .build();

        // Act and Assert
        performCreatePlaceRequest(createPlaceRequestIn)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lugar novo"))
                .andExpect(jsonPath("$.address").value("Rua XX"))
                .andExpect(jsonPath("$.city").value("Porto Alegre"))
                .andExpect(jsonPath("$.country").value("Brazil"))
                .andExpect(jsonPath("$.latitude").value(30.0319164))
                .andExpect(jsonPath("$.longitude").value(-51.210757))
                .andExpect(jsonPath("$.slug").value("lugar-novo"))
                .andExpect(jsonPath("$.category").value("COFFEE"))
                .andDo(print());
    }

    @Test
    @DisplayName("should get list of places")
    void testGet() throws Exception {
        // Arrange
        var placeResponseOut = PlaceResponseOut.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .name("Test Place")
                .imageUrl("https://example.com/image.jpg")
                .description("A beautiful test place")
                .address("123 Test St")
                .city("Testville")
                .country("Testland")
                .latitude(10.123456)
                .longitude(20.654321)
                .slug("test-place")
                .phone("+1234567890")
                .category(List.of(PlaceCategory.COFFEE))
                .rating(4.5)
                .build();
        var placeResponseOutList = List.of(placeResponseOut);
        var responseBody = objectMapper.writeValueAsString(placeResponseOutList);

        // Act and Assert
        performGetPlacesRequest()
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Test
    @DisplayName("should get place by id")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById() throws Exception {
        // Arrange
        UUID placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var placeResponseOut = PlaceResponseOut.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .name("Test Place")
                .imageUrl("https://example.com/image.jpg")
                .description("A beautiful test place")
                .address("123 Test St")
                .city("Testville")
                .country("Testland")
                .latitude(10.123456)
                .longitude(20.654321)
                .slug("test-place")
                .phone("+1234567890")
                .category(List.of(PlaceCategory.COFFEE))
                .rating(4.5)
                .build();
        var responseBody = objectMapper.writeValueAsString(placeResponseOut);

        // Act and Assert
        performGetPlaceByIdRequest(placeUuid)
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody))
                .andDo(print());

    }

    @Test
    @DisplayName("should update place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        UUID placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var updatePlaceRequestIn = UpdatePlaceRequestIn.builder()
                .name("Test Edited")
                .description("A beautiful edited test place")
                .city("Editville")
                .country("Editland")
                .latitude(11.123456)
                .longitude(21.654321)
                .build();
        var placeResponseOut = PlaceResponseOut.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .name("Test Edited")
                .description("A beautiful edited test place")
                .imageUrl("https://example.com/image.jpg")
                .address("123 Test St")
                .city("Editville")
                .country("Editland")
                .latitude(11.123456)
                .longitude(21.654321)
                .slug("test-place")
                .phone("+1234567890")
                .category(List.of(PlaceCategory.COFFEE))
                .rating(4.5)
                .build();
        var responseBody = objectMapper.writeValueAsString(placeResponseOut);

        // Act and Assert
        performUpdatePlaceRequest(placeUuid, updatePlaceRequestIn)
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    private ResultActions performCreatePlaceRequest(Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post("/api/v1/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private ResultActions performGetPlacesRequest() throws Exception {
        return mockMvc.perform(get("/api/v1/place")
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetPlaceByIdRequest(UUID placeUuid) throws Exception {
        return mockMvc.perform(get("/api/v1/place/" + placeUuid)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performUpdatePlaceRequest(UUID placeUuid, Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(put("/api/v1/place/" + placeUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

}