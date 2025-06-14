package com.dsllt.oTravel_api.infra.adapter.review.out.persistence;

import com.dsllt.oTravel_api.domain.review.port.out.ReviewPort;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceEntity;
import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.CreateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.ReviewResponse;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.UpdateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.out.persistence.jpa.ReviewEntity;
import com.dsllt.oTravel_api.infra.adapter.review.out.persistence.jpa.ReviewJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserEntity;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ReviewRepository implements ReviewPort {

    private final ReviewJpaRepository reviewJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public ReviewResponse create(CreateReviewRequestIn createReviewRequestIn) {
        var place = placeJpaRepository.findById(UUID.fromString(createReviewRequestIn.placeId())).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
        var user = userJpaRepository.findById(UUID.fromString(createReviewRequestIn.userId()))
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
        var reviewEntity = ReviewEntity.builder()
                .description(createReviewRequestIn.description())
                .rating(createReviewRequestIn.rating())
                .placeId(UUID.fromString(createReviewRequestIn.placeId()))
                .userId(UUID.fromString(createReviewRequestIn.userId()))
                .build();
        var persistedReview = reviewJpaRepository.save(reviewEntity);

        return ReviewResponse.builder()
                .placeName(place.getName())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .description(persistedReview.getDescription())
                .rating(persistedReview.getRating())
                .build();
    }

    @Override
    public List<ReviewResponse> get() {
        var savedReviews = reviewJpaRepository.findAll();

        return savedReviews.stream()
                .map(reviewEntity -> {
                    var place = placeJpaRepository.findById(reviewEntity.getPlaceId()).orElse(PlaceEntity.builder().build());
                    var user = userJpaRepository.findById(reviewEntity.getUserId()).orElse(UserEntity.builder().build());

                    return ReviewResponse.builder()
                            .placeName(place.getName())
                            .userFirstName(user.getFirstName())
                            .userLastName(user.getLastName())
                            .description(reviewEntity.getDescription())
                            .rating(reviewEntity.getRating())
                            .build();
                }).toList();
    }

    @Override
    public ReviewResponse getReviewById(UUID reviewUuid) {
        var reviewEntity = reviewJpaRepository.findById(reviewUuid).orElseThrow(() -> new ObjectNotFoundException("Review not found"));
        var placeEntity = placeJpaRepository.findById(reviewEntity.getPlaceId()).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
        var userEntity = userJpaRepository.findById(reviewEntity.getUserId()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        return ReviewResponse.builder()
                .placeName(placeEntity.getName())
                .userFirstName(userEntity.getFirstName())
                .userLastName(userEntity.getLastName())
                .description(reviewEntity.getDescription())
                .rating(reviewEntity.getRating())
                .build();
    }

    @Override
    public ReviewResponse updateReview(UUID reviewUuid, UpdateReviewRequestIn updateReviewRequestIn) {
        var reviewEntity = reviewJpaRepository.findById(reviewUuid).orElseThrow(() -> new ObjectNotFoundException("Review not found"));
        var placeEntity = placeJpaRepository.findById(reviewEntity.getPlaceId()).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
        var userEntity = userJpaRepository.findById(reviewEntity.getUserId()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        var updatedReview = reviewEntity.toBuilder()
                .description(updateReviewRequestIn.description() != null ? updateReviewRequestIn.description() : reviewEntity.getDescription())
                .rating(updateReviewRequestIn.rating() != null ? updateReviewRequestIn.rating() : reviewEntity.getRating())
                .build();
        var persistedReview = reviewJpaRepository.save(updatedReview);

        return ReviewResponse.builder()
                .placeName(placeEntity.getName())
                .userFirstName(userEntity.getFirstName())
                .userLastName(userEntity.getLastName())
                .description(persistedReview.getDescription())
                .rating(persistedReview.getRating())
                .build();
    }

    @Override
    public void deleteReview(UUID reviewUuid) {
        reviewJpaRepository.deleteById(reviewUuid);
    }
}
