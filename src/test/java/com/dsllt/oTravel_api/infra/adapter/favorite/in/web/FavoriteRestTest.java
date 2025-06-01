package com.dsllt.oTravel_api.infra.adapter.favorite.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.favorite.model.Favorite;
import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import com.dsllt.oTravel_api.domain.user.model.UserRole;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.CreateFavoriteRequestIn;
import com.dsllt.oTravel_api.infra.adapter.favorite.in.web.model.FavoriteResponseOut;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FavoriteRestTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    UUID userUuid;
    UUID placeUuid;
    UUID favoriteUuid;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM schedules");
    }

    @BeforeEach
    void setup() {
        userUuid = UUID.fromString("a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b");
        placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        favoriteUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
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
        jdbcTemplate.execute("""
                    INSERT INTO users (
                        id,
                        first_name,
                        last_name,
                        email,
                        image,
                        password,
                        role,
                        created_at
                    ) VALUES (
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b',
                        'João',
                        'Silva',
                        'joao.silva@example.com',
                        'https://example.com/images/joao.jpg',
                        '$2a$10$7dQ9xZL7XQb7JMe5vEfnQu0wTRcz2e0x3RPpK3Vb09YBpUOJ6vK6a', -- hash de senha (ex: BCrypt)
                        'USER',
                        CURRENT_TIMESTAMP
                    );
                """);
        jdbcTemplate.execute("""
                INSERT INTO favorites (id, user_id, place_id, active)
                VALUES ('550e8400-e29b-41d4-a716-446655440000','a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b', '550e8400-e29b-41d4-a716-446655440000', true)
                """);
    }

    @Test
    @DisplayName("should register user favorite successfully")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        UUID placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID userUuid = UUID.fromString("a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b");
        var createFavoriteRequestIn = CreateFavoriteRequestIn.builder()
                .placeId(placeUuid)
                .userId(userUuid)
                .build();

        // Act and Assert
        performCreateFavoriteRequest(createFavoriteRequestIn)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userUuid.toString()))
                .andExpect(jsonPath("$.placeId").value(placeUuid.toString()))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("should throw error when trying to register favorite with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var createFavoriteRequestIn = CreateFavoriteRequestIn.builder()
                .build();

        // Act and Assert
        performCreateFavoriteRequest(createFavoriteRequestIn)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("should get a list of users favorites")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetByUserId() throws Exception {
        // Arrange
        var userResponseOut = createUserResponseOut();
        var placeResponseOut = createPlaceResponseOut();
        var favoriteResponseOut = createFavoriteResponseOut(userResponseOut, placeResponseOut);
        String responseBody = objectMapper.writeValueAsString(favoriteResponseOut);

        // Act and Assert
        performGetFavoriteByUserRequest(userUuid.toString())
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody))
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("should allow to update favorites active status")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        var favorite = Favorite.builder()
                .id(favoriteUuid)
                .userId(userUuid)
                .placeId(placeUuid)
                .active(false)
                .build();
        var responseBody = objectMapper.writeValueAsString(favorite);

        // Act and Assert
        performPutFavoriteByUserRequest(userUuid.toString(), placeUuid.toString())
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    @DisplayName("should allow to update favorites active status correctly when changing it for the second time")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate2() throws Exception {
        // Arrange
        var favoriteFirstResponse = Favorite.builder()
                .id(favoriteUuid)
                .userId(userUuid)
                .placeId(placeUuid)
                .active(false)
                .build();
        var favoriteSecondResponse = Favorite.builder()
                .id(favoriteUuid)
                .userId(userUuid)
                .placeId(placeUuid)
                .active(true)
                .build();
        var favoriteFirstResponseBody = objectMapper.writeValueAsString(favoriteFirstResponse);
        var favoriteSecondResponseBody = objectMapper.writeValueAsString(favoriteSecondResponse);

        // Act and Assert
        performPutFavoriteByUserRequest(userUuid.toString(), placeUuid.toString())
                .andExpect(status().isOk())
                .andExpect(content().json(favoriteFirstResponseBody));
        performPutFavoriteByUserRequest(userUuid.toString(), placeUuid.toString())
                .andExpect(status().isOk())
                .andExpect(content().json(favoriteSecondResponseBody));
    }

    @Test
    @DisplayName("should retrieve all users with saved favorites and its favorites")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetAllUsersWithActiveFavorites() throws Exception {
        // Arrange
        createNewPlaceInDatabase();
        createNewUserInDatabase();
        createFavoritesForNewUserInDatabase();
        var userResponseOut = createUserResponseOut();
        var placeResponseOut = createPlaceResponseOut();
        var firstUserfavoriteResponseOut = createFavoriteResponseOut(userResponseOut, placeResponseOut);
        var user2ResponseOut = createUser2ResponseOut();
        var place2ResponseOut = createPlace2ResponseOut();
        var secontUserfavoriteResponseOut = createFavorite2ResponseOut(user2ResponseOut, placeResponseOut, place2ResponseOut);
        String responseBody = objectMapper.writeValueAsString(List.of(firstUserfavoriteResponseOut, secontUserfavoriteResponseOut));

        // Act and Assert
        performGetActiveFavorites()
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    private FavoriteResponseOut createFavorite2ResponseOut(CreateUserResponseOut user2ResponseOut, PlaceResponseOut placeResponseOut, PlaceResponseOut place2ResponseOut) {
        return FavoriteResponseOut.builder()
                .user(user2ResponseOut)
                .favorites(List.of(placeResponseOut, place2ResponseOut))
                .build();
    }

    private void createFavoritesForNewUserInDatabase() {
        jdbcTemplate.execute("""
                INSERT INTO favorites (id, user_id, place_id, active)
                VALUES ('550e8400-e29b-41d4-a716-446655440001','a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c', '550e8400-e29b-41d4-a716-446655440000', true)
                """);
        jdbcTemplate.execute("""
                INSERT INTO favorites (id, user_id, place_id, active)
                VALUES ('550e8400-e29b-41d4-a716-446655440002','a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c', '550e8400-e29b-41d4-a716-446655440001', true)
                """);
    }

    private void createNewUserInDatabase() {
        jdbcTemplate.execute("""
                    INSERT INTO users (
                        id,
                        first_name,
                        last_name,
                        email,
                        image,
                        password,
                        role,
                        created_at
                    ) VALUES (
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c',
                        'João',
                        'Silva 2',
                        'joao.silva2@example.com',
                        'https://example.com/images/joao.jpg',
                        '$2a$10$7dQ9xZL7XQb7JMe5vEfnQu0wTRcz2e0x3RPpK3Vb09YBpUOJ6vK6a',
                        'USER',
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    private void createNewPlaceInDatabase() {
        jdbcTemplate.execute("""
                    INSERT INTO places (
                        id, name, image_url, description, address,
                        city, country, latitude, longitude, slug,
                        phone, category, rating, created_at
                    ) VALUES (
                        '550e8400-e29b-41d4-a716-446655440001',
                        'Test Place 2',
                        'https://example.com/image.jpg',
                        'A beautiful test place',
                        '123 Test St',
                        'Testville',
                        'Testland',
                        10.123456,
                        20.654321,
                        'test-place2',
                        '+1234567890',
                        'COFFEE',
                        4.5,
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    private FavoriteResponseOut createFavoriteResponseOut(CreateUserResponseOut userResponseOut, PlaceResponseOut placeResponseOut) {
        return FavoriteResponseOut.builder()
                .user(userResponseOut)
                .favorites(List.of(placeResponseOut))
                .build();
    }

    private PlaceResponseOut createPlaceResponseOut() {
        return PlaceResponseOut.builder()
                .id(placeUuid)
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
    }

    private PlaceResponseOut createPlace2ResponseOut() {
        return PlaceResponseOut.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                .name("Test Place 2")
                .imageUrl("https://example.com/image.jpg")
                .description("A beautiful test place")
                .address("123 Test St")
                .city("Testville")
                .country("Testland")
                .latitude(10.123456)
                .longitude(20.654321)
                .slug("test-place2")
                .phone("+1234567890")
                .category(List.of(PlaceCategory.COFFEE))
                .rating(4.5)
                .build();
    }

    private CreateUserResponseOut createUserResponseOut() {
        return CreateUserResponseOut.builder()
                .id(userUuid)
                .firstName("João")
                .lastName("Silva")
                .email("joao.silva@example.com")
                .image("https://example.com/images/joao.jpg")
                .role(UserRole.USER)
                .build();
    }

    private CreateUserResponseOut createUser2ResponseOut() {
        return CreateUserResponseOut.builder()
                .id(UUID.fromString("a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c"))
                .firstName("João")
                .lastName("Silva 2")
                .email("joao.silva2@example.com")
                .image("https://example.com/images/joao.jpg")
                .role(UserRole.USER)
                .build();
    }

    private ResultActions performCreateFavoriteRequest(Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post("/api/v1/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private ResultActions performGetFavoriteByUserRequest(String userUuid) throws Exception {
        return mockMvc.perform(get("/api/v1/favorite/" + userUuid)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetActiveFavorites() throws Exception {
        return mockMvc.perform(get("/api/v1/favorite/active")
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performPutFavoriteByUserRequest(String userUuid, String placeUuid) throws Exception {
        return mockMvc.perform(put("/api/v1/favorite/" + userUuid + "?placeId=" + placeUuid)
                .contentType(MediaType.APPLICATION_JSON));
    }
}