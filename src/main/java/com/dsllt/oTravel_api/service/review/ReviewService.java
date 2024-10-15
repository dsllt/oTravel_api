package com.dsllt.oTravel_api.service.review;

import com.dsllt.oTravel_api.dtos.review.CreateReviewDTO;
import com.dsllt.oTravel_api.dtos.review.ReviewDTO;
import com.dsllt.oTravel_api.entity.review.Review;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    Review save(@Valid CreateReviewDTO createReviewDTO);
    List<Review> get();
    Review getReviewById(UUID reviewUuid);
    Review updateReview(UUID reviewUuid, CreateReviewDTO createReviewDTO);
    void deleteReview(UUID reviewUuid);

}
