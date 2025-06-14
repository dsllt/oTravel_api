package com.dsllt.oTravel_api.domain.review.service;

import com.dsllt.oTravel_api.domain.review.model.Review;
import com.dsllt.oTravel_api.domain.review.port.in.ReviewUseCase;
import com.dsllt.oTravel_api.domain.review.port.out.ReviewPort;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.CreateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.ReviewResponse;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.UpdateReviewRequestIn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewService implements ReviewUseCase {
    private ReviewPort reviewPort;

    @Override
    public ReviewResponse save(CreateReviewRequestIn createReviewRequestIn) {
        return reviewPort.create(createReviewRequestIn);
    }

    @Override
    public List<ReviewResponse> get() {

        return reviewPort.get();
    }

    @Override
    public ReviewResponse getReviewById(UUID reviewUuid) {
        return reviewPort.getReviewById(reviewUuid);
    }

    @Override
    public ReviewResponse updateReview(UUID reviewUuid, UpdateReviewRequestIn updateReviewRequestIn) {
        return reviewPort.updateReview(reviewUuid, updateReviewRequestIn);
    }

    @Override
    public void deleteReview(UUID reviewUuid) {
        reviewPort.deleteReview(reviewUuid);
    }
}
