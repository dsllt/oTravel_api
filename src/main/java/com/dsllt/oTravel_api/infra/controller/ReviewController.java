package com.dsllt.oTravel_api.infra.controller;

import com.dsllt.oTravel_api.core.usecase.ReviewService;
import com.dsllt.oTravel_api.infra.dto.review.CreateReviewDTO;
import com.dsllt.oTravel_api.infra.dto.review.ReviewDTO;
import com.dsllt.oTravel_api.core.entity.review.Review;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody @Valid CreateReviewDTO createReviewDTO, UriComponentsBuilder uriComponentsBuilder){
        Review savedReview = reviewService.save(createReviewDTO);
        System.out.println("VEM DE ID "+savedReview.getId());

        var uri = uriComponentsBuilder.path("/api/v1/review/{reviewUuid}").buildAndExpand(savedReview.getId()).toUri();

        return ResponseEntity.created(uri).body(ReviewDTO.from(savedReview));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> get(){
        List<Review> reviews = reviewService.get();
        List<ReviewDTO> reviewsDTO = reviews.stream().map(ReviewDTO::from).collect(Collectors.toList());

        return ResponseEntity.ok().body(reviewsDTO);
    }

    @GetMapping("/{reviewUuid}")
    public ResponseEntity<ReviewDTO> getReviewById(@Nonnull @PathVariable UUID reviewUuid){
        Review review = reviewService.getReviewById(reviewUuid);

        return ResponseEntity.ok().body(ReviewDTO.from(review));
    }

    @PutMapping("/{reviewUuid}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String reviewUuid, @RequestBody CreateReviewDTO createReviewDTO){
        Review updatedReview = reviewService.updateReview(UUID.fromString(reviewUuid), createReviewDTO);

        return ResponseEntity.ok().body(ReviewDTO.from(updatedReview));
    }
    @DeleteMapping("/{reviewUuid}")
    public ResponseEntity<Review> deleteReview(@PathVariable String reviewUuid){
        reviewService.deleteReview(UUID.fromString(reviewUuid));

        return ResponseEntity.noContent().build();
    }
}
