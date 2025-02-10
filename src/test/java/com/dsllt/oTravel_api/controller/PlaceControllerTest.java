package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.infra.dto.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.core.usecase.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PlaceService placeService;
    @MockBean
    PlaceRepository placeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
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
        String json = """
                 {
                	"country": "Brazil",
                	"address": "Rua XX",
                	"city": "Porto Alegre",
                	"latitude":-30.0319164,
                	"name": "Lugar novo",
                	"category":["COFFEE"],
                	"slug": "lugar-novo",
                	"longitude": -51.2107576
                }
                """;
        UUID placeId = UUID.randomUUID();
        PlaceDTO mockedPlaceDTO = new PlaceDTO(placeId, "Lugar novo", "", "Description", "Rua XX", "Porto Alegre", "Brazil", -30.0319164,-51.2107576,"lugar-novo", "", new ArrayList<>(), 5.0);
        when(placeService.save(any(CreatePlaceDTO.class))).thenReturn(mockedPlaceDTO);

        // Act
        var response = mockMvc.perform(
                post("/api/v1/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("should get list of places")
    void testGet() throws Exception {
        // Arrange

        // Act
        var response = mockMvc.perform(
                get("/api/v1/place")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should get place by id")
    void testGetById() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        PlaceDTO mockedPlaceDTO = new PlaceDTO(placeId, "Lugar novo", "", "Description", "Rua XX", "Porto Alegre", "Brazil", -30.0319164,-51.2107576,"lugar-novo", "", new ArrayList<>(), 5.0);

        when(placeService.getPlaceById(placeId)).thenReturn(mockedPlaceDTO);

        // Act
        var response = mockMvc.perform(
                get("/api/v1/place/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should update place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        String json = """
                 {
                	"country": "Brazil",
                	"address": "Rua XX",
                	"city": "Porto Alegre",
                	"latitude":-30.0319164,
                	"name": "Lugar novo",
                	"category":[ "COFFEE"],
                	"slug": "lugar-novo",
                	"longitude": -51.2107576
                }
                """;
        Place mockedPlace = Place.builder()
                .id(placeId)
                .name("Name")
                .build();
        PlaceDTO mockedPlaceDTO = new PlaceDTO(placeId, "Lugar novo", "", "Description", "Rua XX", "Porto Alegre", "Brazil", -30.0319164,-51.2107576,"lugar-novo", "", new ArrayList<>(), 5.0);
        PlaceDTO updatedMockedPlaceDTO = new PlaceDTO(placeId, "Lugar novo 1", "", "Description", "Rua XX", "Porto Alegre", "Brazil", -30.0319164,-51.2107576,"lugar-novo", "", new ArrayList<>(), 5.0);

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(mockedPlace));
        when(placeService.updatePlace(placeId, mockedPlaceDTO)).thenReturn(updatedMockedPlaceDTO);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/place/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)

        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should filter places")
    void testFilter() throws Exception {
        // Arrange
        String name = "Lugar novo";
        String city = "Porto Alegre";

        // Act
        var response = mockMvc.perform(
                get("/api/v1/place/filter")
                        .param("name", name)
                        .param("city", city)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

}