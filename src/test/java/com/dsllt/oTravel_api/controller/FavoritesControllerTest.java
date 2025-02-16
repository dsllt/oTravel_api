package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.core.usecase.FavoriteService;
import com.dsllt.oTravel_api.core.usecase.PlaceService;
import com.dsllt.oTravel_api.core.usecase.UserService;
import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import com.dsllt.oTravel_api.infra.dto.favorite.UserFavoritesDTO;
import com.dsllt.oTravel_api.infra.dto.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.infra.enums.PlaceCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    @Autowired
    private WebApplicationContext context;

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
        favoriteService.save(createFavoriteDTO);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/favorite/" + testUser.id() + "?placeUuid=" + testPlace.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("john").authorities(new SimpleGrantedAuthority("ROLE_USER")))
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should allow to update favorites active status correctly when changing it for the second time")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate2() throws Exception {
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace = placeService.save(createTestPlace);
        CreateFavoriteDTO createFavoriteDTO = new CreateFavoriteDTO(testUser.id(),testPlace.id());
        favoriteService.save(createFavoriteDTO);

        // Act
        var response1 = mockMvc.perform(
                put("/api/v1/favorite/" + testUser.id() + "?placeUuid=" + testPlace.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        var response2 = mockMvc.perform(
                put("/api/v1/favorite/" + testUser.id() + "?placeUuid=" + testPlace.id())
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

    @Test
    @DisplayName("should retrieve all users with saved favorites and its favorites")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetAllUsersWithActiveFavorites() throws Exception{
        // Arrange
        CreateUserDTO createTestUser1 = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO mockUser1 = userService.save(createTestUser1);
        CreateUserDTO createTestUser2 = new CreateUserDTO("John", "Doe", "jeohndoe2@email.com", "","123456");
        UserDTO mockUser2 = userService.save(createTestUser2);
        CreatePlaceDTO createTestPlace1 = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace1 = placeService.save(createTestPlace1);
        CreatePlaceDTO createTestPlace2 = new CreatePlaceDTO("Test Place2", "", "", "Address", "City", "Country", -30.01,-30.01, "test2", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace2 = placeService.save(createTestPlace2);
        CreateFavoriteDTO createFavoriteDTO1 = new CreateFavoriteDTO(mockUser1.id(),testPlace1.id());
        favoriteService.save(createFavoriteDTO1);
        CreateFavoriteDTO createFavoriteDTO2 = new CreateFavoriteDTO(mockUser2.id(),testPlace2.id());
        favoriteService.save(createFavoriteDTO2);
        CreateFavoriteDTO createFavoriteDTO3 = new CreateFavoriteDTO(mockUser2.id(),testPlace1.id());
        favoriteService.save(createFavoriteDTO3);

        // Act
        var response = mockMvc.perform(
                get("/api/v1/favorite/active")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserFavoritesDTO> userWithFavorites = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(2, userWithFavorites.size());
        UserFavoritesDTO user1 = userWithFavorites.stream().filter(u -> u.user().id().equals(mockUser1.id())).findFirst().orElse(null);
        UserFavoritesDTO user2 = userWithFavorites.stream().filter(u -> u.user().id().equals(mockUser2.id())).findFirst().orElse(null);
        assertNotNull(user1);
        assertNotNull(user2);
        assertEquals(1, user1.favorites().size());
        assertEquals(2, user2.favorites().size());
    }

    @Test
    @DisplayName("should indicate if favorite exists and is active")
    @WithMockUser(value = "jeohndoe@email.com", authorities = "ROLE_USER")
    void testIsFavoriteActive() throws Exception{
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);
        CreatePlaceDTO createTestPlace1 = new CreatePlaceDTO("Test Place", "", "", "Address", "City", "Country", -30.01,-30.01, "test", "", List.of(PlaceCategory.valueOf("COFFEE")));
        PlaceDTO testPlace = placeService.save(createTestPlace1);
        CreateFavoriteDTO createFavoriteDTO = new CreateFavoriteDTO(testUser.id(),testPlace.id());
        favoriteService.save(createFavoriteDTO);

        // Act
        var response = mockMvc.perform(
                get("/api/v1/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("placeId", testPlace.id().toString())
                        .param("userId", testUser.id().toString())
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.getContentAsString());
        assertTrue(responseBody.has("isActive"));
        assertTrue(responseBody.get("isActive").asBoolean());
    }

}

