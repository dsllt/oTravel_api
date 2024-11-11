package com.dsllt.oTravel_api.service.review;

import com.dsllt.oTravel_api.dtos.CustomPageDTO;
import com.dsllt.oTravel_api.dtos.review.CreateReviewDTO;
import com.dsllt.oTravel_api.entity.review.Review;
import com.dsllt.oTravel_api.entity.review.ReviewFilter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    Review save(@Valid CreateReviewDTO createReviewDTO);
    List<Review> get();
    Review getReviewById(UUID reviewUuid);
    Review updateReview(UUID reviewUuid, CreateReviewDTO createReviewDTO);
    void deleteReview(UUID reviewUuid);
    CustomPageDTO<Review> filter(ReviewFilter reviewFilter, Pageable pageable);

}
