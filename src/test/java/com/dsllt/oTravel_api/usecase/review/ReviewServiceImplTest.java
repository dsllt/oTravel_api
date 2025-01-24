package com.dsllt.oTravel_api.usecase.review;

import com.dsllt.oTravel_api.core.usecase.ReviewService;
import com.dsllt.oTravel_api.infra.dto.review.CreateReviewDTO;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.review.Review;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.ReviewRepository;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    ReviewService reviewService;

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    @BeforeEach
    public void setup(){
        this.reviewService = new ReviewService(reviewRepository, placeRepository, userRepository);
    }

    @Test
    @DisplayName("should save review")
    public void saveReviewTest() {
        // Arrange
        UUID placeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateReviewDTO createReviewDTO = new CreateReviewDTO(
                "description",
                5.0,
                placeId.toString(),
                userId.toString()
        );
        Place newPlace = Place.builder()
                .id(placeId)
                .name("The Coffee")
                .description("description")
                .address("Rua Fernandes Vieira")
                .city("Porto Alegre")
                .country("Brasil")
                .latitude(-30.0319164)
                .longitude(-51.2107576)
                .slug("the-coffee-bom-fim")
                .createdAt(LocalDateTime.now())
                .build();
        User newUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("joe@test.com")
                .password("123456")
                .createdAt(LocalDateTime.now())
                .build();
        Review repositorySavedReview = Review.builder()
                .id(UUID.randomUUID())
                .description(createReviewDTO.description())
                .rating(createReviewDTO.rating())
                .place(newPlace)
                .user(newUser)
                .createdAt(LocalDateTime.now())
                .build();
        // Act
        when(reviewRepository.save(any(Review.class))).thenReturn(repositorySavedReview);
        when(placeRepository.findById(placeId)).thenReturn(Optional.ofNullable(newPlace));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(newUser));

        Review savedReview = reviewService.save(createReviewDTO);

        // Assert
        then(reviewRepository).should().save(reviewArgumentCaptor.capture());
        Review capturedReview = reviewArgumentCaptor.getValue();
        assertThat(capturedReview.getDescription()).isEqualTo(savedReview.getDescription());
        assertThat(capturedReview.getPlace()).isEqualTo(savedReview.getPlace());
        assertThat(capturedReview.getUser()).isEqualTo(savedReview.getUser());
        verify(reviewRepository, times(1)).save(reviewArgumentCaptor.capture());
    }

    @Test
    @DisplayName("should retrieve all reviews")
    public void getReviewsTest() {
        // Arrange
        Review review = new Review();
        List<Review> reviewList = Arrays.asList(review);
        when(reviewRepository.findAll()).thenReturn(reviewList);

        // Act
        List<Review> retrievedReviews = reviewService.get();

        // Assert
        assertThat(retrievedReviews).isNotEmpty();
        assertThat(retrievedReviews).isInstanceOf(List.class);
        assertThat(retrievedReviews.get(0)).isInstanceOf(Review.class);
        assertThat(retrievedReviews).allMatch(Objects::nonNull);
    }

    @Test
    @DisplayName("should retrieve paginated list of reviews")
    public void getReviewsPaginatedTest() {
        // Arrange
        Review review = new Review();
        List<Review> reviewList = Arrays.asList(review);
        when(reviewRepository.findAll()).thenReturn(reviewList);

        // Act
        List<Review> retrievedReviews = reviewService.get();

        // Assert
        assertThat(retrievedReviews).isNotEmpty();
        assertThat(retrievedReviews).isInstanceOf(List.class);
        assertThat(retrievedReviews.get(0)).isInstanceOf(Review.class);
        assertThat(retrievedReviews).allMatch(Objects::nonNull);
    }

    @Test
    @DisplayName("should retrieve review by id")
    public void getReviewByIdTest() {
        // Arrange
        UUID reviewUuid = UUID.randomUUID();
        Review savedReview = Review.builder()
                .id(reviewUuid)
                .description("Review description")
                .rating(5.0)
                .place(new Place())
                .user(new User())
                .createdAt(LocalDateTime.now())
                .build();
        when(reviewRepository.findById(reviewUuid)).thenReturn(Optional.of(savedReview));

        // Act
        Review retrievedReview = reviewService.getReviewById(reviewUuid);

        // Assert
        assertThat(retrievedReview).isNotNull();
        assertThat(retrievedReview).isInstanceOf(Review.class);
        assertThat(retrievedReview.getId()).isEqualTo(savedReview.getId());
        assertThat(retrievedReview.getDescription()).isEqualTo(savedReview.getDescription());
        assertThat(retrievedReview.getRating()).isEqualTo(savedReview.getRating());
        assertThat(retrievedReview.getPlace()).isEqualTo(savedReview.getPlace());
        assertThat(retrievedReview.getUser()).isEqualTo(savedReview.getUser());
        assertThat(retrievedReview.getCreatedAt()).isEqualTo(savedReview.getCreatedAt());
    }

    @Test
    @DisplayName("should throw ObjectNotFoundException when receiving non-existing id")
    public void getReviewByIdErrorTest() {
        // Arrange
        UUID reviewUuid = UUID.randomUUID();
        when(reviewRepository.findById(reviewUuid)).thenThrow(new ObjectNotFoundException("Review não encontrada."));

        // Act
        assertThrows(ObjectNotFoundException.class, () -> reviewService.getReviewById(reviewUuid));
    }

    @Test
    @DisplayName("should update review")
    public void updateReviewTest() {
        // Arrange
        UUID reviewUuid = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateReviewDTO createReviewDTO = new CreateReviewDTO(
                "description",
                5.0,
                placeId.toString(),
                userId.toString()
        );
        Review retrievedReview = Review.builder()
                .id(reviewUuid)
                .description("Old description")
                .rating(3.0)
                .place(new Place())
                .user(new User())
                .createdAt(LocalDateTime.now())
                .build();
        Place newPlace = Place.builder()
                .id(placeId)
                .name("The Coffee")
                .description("description")
                .address("Rua Fernandes Vieira")
                .city("Porto Alegre")
                .country("Brasil")
                .latitude(-30.0319164)
                .longitude(-51.2107576)
                .slug("the-coffee-bom-fim")
                .createdAt(LocalDateTime.now())
                .build();
        User newUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("doe@email.com")
                .password("123456")
                .createdAt(LocalDateTime.now())
                .build();
        Review updatedReview = Review.builder()
                .id(reviewUuid)
                .description(createReviewDTO.description())
                .rating(createReviewDTO.rating())
                .place(newPlace)
                .user(newUser)
                .createdAt(LocalDateTime.now())
                .build();
        when(reviewRepository.findById(reviewUuid)).thenReturn(Optional.of(retrievedReview));
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(newPlace));
        when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        // Act
        Review updateReviewTest = reviewService.updateReview(reviewUuid, createReviewDTO);

        // Assert
        assertThat(updateReviewTest).isNotNull();
        assertThat(updateReviewTest).isInstanceOf(Review.class);
        assertThat(updateReviewTest.getId()).isEqualTo(retrievedReview.getId());
        assertThat(updateReviewTest.getDescription()).isEqualTo(createReviewDTO.description());
        assertThat(updateReviewTest.getRating()).isEqualTo(createReviewDTO.rating());
        assertThat(updateReviewTest.getPlace()).isEqualTo(newPlace);
        assertThat(updateReviewTest.getUser()).isEqualTo(newUser);
}
}