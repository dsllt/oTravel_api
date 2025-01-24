package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.infra.dto.review.CreateReviewDTO;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.review.Review;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.ReviewRepository;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
import com.dsllt.oTravel_api.core.usecase.review.ReviewServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ReviewServiceImpl reviewService;
    @MockBean
    PlaceRepository placeRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ReviewRepository reviewRepository;


    @Test
    @DisplayName("should throw exception when trying to register without authorization")
    void testCreateWithoutAuth() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(403, response.getStatus());
    }

    @Test
    @DisplayName("should throw exception when trying to register a review with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should create review")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        String json = """
                 {
                 	"description": "Muito bom",
                 	"rating": 5,
                 	"placeId": "%s",
                 	"userId": "%s"
                 }
                """.formatted(placeId.toString(), userId.toString());
        Place mockedPlace = Place.builder()
                .id(placeId)
                .name("Name")
                .build();
        User mockedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();
        Review mockedReview = Review.builder()
                .id(reviewId)
                .user(mockedUser)
                .place(mockedPlace)
                .build();

        // Mocking the existence of the user and place
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(mockedPlace));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));
        when(reviewService.save(any(CreateReviewDTO.class))).thenReturn(mockedReview);

        // Act
        var response = mockMvc.perform(
                post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("should get list of reviews")
    void testGet() throws Exception {
        // Arrange
        // Act
        var response = mockMvc.perform(
                get("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should get review by id")
    void testGetById() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        Place mockedPlace = Place.builder()
                .id(placeId)
                .name("Name")
                .build();
        User mockedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();
        Review mockedReview = Review.builder()
                .id(reviewId)
                .user(mockedUser)
                .place(mockedPlace)
                .build();

        when(reviewService.getReviewById(reviewId)).thenReturn(mockedReview);


        // Act
        var response = mockMvc.perform(
                get("/api/v1/review/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should throw error when searching review by invalid id")
    void testGetById2() throws Exception {
        // Arrange
        String reviewUuid = "1";

        // Act
        var response = mockMvc.perform(
                get("/api/v1/review/" + reviewUuid)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should update review")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String json = """
                 {
                 	"description": "Muito bom",
                 	"rating": 5.0,
                 	"placeId": "%s",
                 	"userId": "%s"
                 }
                """.formatted(placeId.toString(), userId.toString());

        Place mockedPlace = Place.builder()
                .id(placeId)
                .name("Name")
                .build();
        User mockedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();
        Review mockedReview = Review.builder()
                .id(reviewId)
                .user(mockedUser)
                .place(mockedPlace)
                .description("Description")
                .build();
        Review updatedMockedReview = Review.builder()
                .id(reviewId)
                .user(mockedUser)
                .place(mockedPlace)
                .description("Muito bom")
                .rating(5.0)
                .build();
        CreateReviewDTO mockCreateReviewDTO = new CreateReviewDTO("Muito bom", 5.0, placeId.toString(), userId.toString());
        // Mocking the existence of the user and place
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(mockedPlace));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockedReview));
        when(reviewService.updateReview(reviewId, mockCreateReviewDTO)).thenReturn(updatedMockedReview);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/review/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }
}