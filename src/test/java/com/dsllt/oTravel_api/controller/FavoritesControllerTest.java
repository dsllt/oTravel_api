package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import com.dsllt.oTravel_api.core.usecase.FavoriteService;
import com.dsllt.oTravel_api.core.usecase.PlaceService;
import com.dsllt.oTravel_api.core.usecase.UserService;
import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import com.dsllt.oTravel_api.infra.dto.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.infra.enums.PlaceCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class FavoritesControllerTest  {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private  PlaceService placeService;
    @Autowired
    private UserService userService;
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
    @DisplayName("should register user favorite successfully")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace = placeService.save(createTestPlace);
        String json = String.format("""
                 {
                	"userId": "%s",
                	"placeId": "%s"
                }
                """, testUser.id(), testPlace.id());

        // Act
        var response = mockMvc.perform(
                post("/api/v1/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(201, response.getStatus());
        String responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains(testUser.id().toString()));
        assertTrue(responseBody.contains(testPlace.id().toString()));
    }

    @Test
    @DisplayName("should throw error when trying to register favorite with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/api/v1/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should get a list of users favorites")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetByUserId() throws Exception {
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace1 = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace1 = placeService.save(createTestPlace1);
        CreatePlaceDTO createTestPlace2 = new CreatePlaceDTO("Test Place2", "", "", "Address", "City", "Country", -30.01,-30.01, "test2", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace2 = placeService.save(createTestPlace2);
        CreateFavoriteDTO createFavoriteDTO1 = new CreateFavoriteDTO(testUser.id(),testPlace1.id());
        favoriteService.save(createFavoriteDTO1);
        CreateFavoriteDTO createFavoriteDTO2 = new CreateFavoriteDTO(testUser.id(),testPlace2.id());
        favoriteService.save(createFavoriteDTO2);

        // Act
        var response = mockMvc.perform(
                get("/api/v1/favorite/" + testUser.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should allow to update favorites active status")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace = placeService.save(createTestPlace);
        CreateFavoriteDTO createFavoriteDTO = new CreateFavoriteDTO(testUser.id(),testPlace.id());
        Favorite mockedFavorite = favoriteService.save(createFavoriteDTO);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/favorite/" + mockedFavorite.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should allow to update favorites active status")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate2() throws Exception {
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace = placeService.save(createTestPlace);
        CreateFavoriteDTO createFavoriteDTO = new CreateFavoriteDTO(testUser.id(),testPlace.id());
        Favorite mockedFavorite = favoriteService.save(createFavoriteDTO);

        // Act
        var response1 = mockMvc.perform(
                put("/api/v1/favorite/" + mockedFavorite.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        var response2 = mockMvc.perform(
                put("/api/v1/favorite/" + mockedFavorite.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response2.getStatus());
        String responseBody1 = response1.getContentAsString();
        String responseBody2 = response2.getContentAsString();
        assertNotNull(responseBody1);
        assertNotNull(responseBody2);
        assertTrue(responseBody1.contains(String.valueOf(false)));
        assertTrue(responseBody2.contains(String.valueOf(true)));
    }


}