package com.dsllt.oTravel_api.infra.adapter.review.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.CreateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.ReviewResponse;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.UpdateReviewRequestIn;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    UUID userUuid;
    UUID placeUuid;

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
    }

    @Test
    @DisplayName("should throw exception when trying to register without authorization")
    void testCreateWithoutAuth() throws Exception {
        // Arrange
        var requestIn = CreateReviewRequestIn.builder().build();

        // Act
        performCreateReviewRequest(requestIn)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should throw exception when trying to register a review with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        var requestIn = CreateReviewRequestIn.builder().build();

        // Act and Assert
        performCreateReviewRequest(requestIn)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should create review")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var requestIn = CreateReviewRequestIn.builder()
                .description("Muito bom")
                .rating(5.0)
                .placeId(placeUuid.toString())
                .userId(userUuid.toString())
                .build();
        var responseOut = ReviewResponse.builder()
                .placeName("Test Place")
                .userFirstName("João")
                .userLastName("Silva")
                .description("Muito bom")
                .rating(5.0)
                .build();
        String responseBody = objectMapper.writeValueAsString(responseOut);
        // Act and Assert
        performCreateReviewRequest(requestIn)
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    @DisplayName("should get list of reviews")
    void testGet() throws Exception {
        // Arrange
        createReviews();
        // Act and assert
        performGetReviewRequest()
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should get review by id")
    void testGetById() throws Exception {
        // Arrange
        UUID reviewId = UUID.fromString("a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c");
        createReviews();

        // Act and Assert
        performGetByIdReviewRequest(reviewId.toString())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should throw error when searching review by invalid id")
    void testGetById2() throws Exception {
        // Arrange
        String reviewUuid = "1";

        // Act and Assert
        performGetByIdReviewRequest(reviewUuid)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should update review")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        UUID reviewId = UUID.fromString("a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c");
        createReviews();
        var updatedRequest = UpdateReviewRequestIn.builder()
                .description("Updated description")
                .build();
        var responseOut = ReviewResponse.builder()
                .userFirstName("João")
                .userLastName("Silva")
                .placeName("Test Place")
                .description("Updated description")
                .rating(5.0)
                .build();

        String responseBody = objectMapper.writeValueAsString(responseOut);

        // Act and Assert
        performUpdateReviewRequest(reviewId.toString(), updatedRequest)
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    private ResultActions performCreateReviewRequest(Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post("/api/v1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private ResultActions performGetReviewRequest() throws Exception {
        return mockMvc.perform(get("/api/v1/review")
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetByIdReviewRequest(String reviewId) throws Exception {
        return mockMvc.perform(get("/api/v1/review/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performUpdateReviewRequest(String reviewId, Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(put("/api/v1/review/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private void createReviews() {
        jdbcTemplate.execute("""
                    INSERT INTO reviews (
                        id,
                        description,
                        rating,
                        place_id,
                        user_id,
                        created_at
                    ) VALUES (
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7c',
                        'Descricao',
                        5.0,
                        '550e8400-e29b-41d4-a716-446655440000',
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b',
                        CURRENT_TIMESTAMP
                    );
                """);
        jdbcTemplate.execute("""
                    INSERT INTO reviews (
                        id,
                        description,
                        rating,
                        place_id,
                        user_id,
                        created_at
                    ) VALUES (
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7f',
                        'Descricao 2',
                        4.0,
                        '550e8400-e29b-41d4-a716-446655440000',
                        'a3f2b1c4-9a4e-4c4d-b8e5-2c3f4d5e6a7b',
                        CURRENT_TIMESTAMP
                    );
                """);
    }
}