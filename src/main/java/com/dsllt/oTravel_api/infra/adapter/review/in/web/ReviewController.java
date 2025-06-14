package com.dsllt.oTravel_api.infra.adapter.review.in.web;

import com.dsllt.oTravel_api.domain.review.model.Review;
import com.dsllt.oTravel_api.domain.review.service.ReviewService;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.CreateReviewRequestIn;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.ReviewResponse;
import com.dsllt.oTravel_api.infra.adapter.review.in.web.model.UpdateReviewRequestIn;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@RequestBody @Valid CreateReviewRequestIn createReviewDTO, UriComponentsBuilder uriComponentsBuilder) {
        var savedReview = reviewService.save(createReviewDTO);
        return ResponseEntity.status(201).body(savedReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> get() {
        var reviews = reviewService.get();

        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/{reviewUuid}")
    public ResponseEntity<ReviewResponse> getReviewById(@Nonnull @PathVariable UUID reviewUuid) {
        var review = reviewService.getReviewById(reviewUuid);

        return ResponseEntity.ok().body(review);
    }

    @PutMapping("/{reviewUuid}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable String reviewUuid, @RequestBody UpdateReviewRequestIn updateReviewRequestIn) {
        var updatedReview = reviewService.updateReview(UUID.fromString(reviewUuid), updateReviewRequestIn);

        return ResponseEntity.ok().body(updatedReview);
    }

    @DeleteMapping("/{reviewUuid}")
    public ResponseEntity<Review> deleteReview(@PathVariable String reviewUuid) {
        reviewService.deleteReview(UUID.fromString(reviewUuid));

        return ResponseEntity.noContent().build();
    }
}
