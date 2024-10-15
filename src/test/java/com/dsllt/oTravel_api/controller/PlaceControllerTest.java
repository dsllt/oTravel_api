package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.service.place.PlaceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    PlaceServiceImpl placeServiceImpl;

    @Test
    @DisplayName("should throw exception when trying to register a place with invalid data")
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
    void testCreate2() throws Exception {
        // Arrange
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
        UUID placeUuid = UUID.randomUUID();

        // Act
        var response = mockMvc.perform(
                get("/api/v1/place/" + placeUuid)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should throw error when searching place by invalid id")
    void testGetById2() throws Exception {
        // Arrange
        String placeUuid = "1";

        // Act
        var response = mockMvc.perform(
                get("/api/v1/place/" + placeUuid)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should update place")
    void testUpdate() throws Exception {
        // Arrange
        UUID placeUuid = UUID.randomUUID();
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

        // Act
        var response = mockMvc.perform(
                put("/api/v1/place/" + placeUuid)
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