package com.dsllt.oTravel_api.domain.review.port.out;

import com.dsllt.oTravel_api.domain.review.model.Review;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.CreateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.ReviewResponse;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.UpdateReviewRequestIn;

import java.util.List;
import java.util.UUID;

public interface ReviewPort {
    ReviewResponse create(CreateReviewRequestIn createReviewRequestIn);
    List<ReviewResponse> get();
    ReviewResponse getReviewById(UUID reviewUuid);
    ReviewResponse updateReview(UUID reviewUuid, UpdateReviewRequestIn updateReviewRequestIn);
    void deleteReview(UUID reviewUuid);
}
