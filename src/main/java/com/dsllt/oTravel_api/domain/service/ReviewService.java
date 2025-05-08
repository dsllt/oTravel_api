package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.infra.dto.review.CreateReviewDTO;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.review.Review;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.ReviewRepository;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public Review save(CreateReviewDTO createReviewDTO) {
        Place retrievedPlace = placeRepository.findById(UUID.fromString(createReviewDTO.placeId())).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
        User retrievedUser = userRepository.findById(UUID.fromString(createReviewDTO.userId())).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        Review newReview = new Review(createReviewDTO, retrievedUser, retrievedPlace);
        return reviewRepository.save(newReview);
    }

    public List<Review> get() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(UUID reviewUuid) {
        return reviewRepository.findById(reviewUuid).orElseThrow(() -> new ObjectNotFoundException("Review não encontrada."));
    }

    public Review updateReview(UUID reviewUuid, CreateReviewDTO createReviewDTO) {
        Review updatedReviewData = validateReviewUpdateData(reviewUuid, createReviewDTO);
        return reviewRepository.save(updatedReviewData);
    }

    public void deleteReview(UUID reviewUuid) {
        reviewRepository.deleteById(reviewUuid);
    }

    private Review validateReviewUpdateData(UUID reviewUuid, CreateReviewDTO createReviewDTO){
        Review retrievedReview = reviewRepository.findById(reviewUuid).orElseThrow(() -> new ObjectNotFoundException("Review não encontrada."));
        if(createReviewDTO.description() != null){
            retrievedReview.setDescription(createReviewDTO.description());
        }
        if(createReviewDTO.rating() != null){
            retrievedReview.setRating(createReviewDTO.rating());
        }
        if(createReviewDTO.placeId() != null){
            Place retrievedPlace = placeRepository.findById(UUID.fromString(createReviewDTO.placeId())).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
            retrievedReview.setPlace(retrievedPlace);
        }
        if (createReviewDTO.userId() != null){
            User retrievedUser = userRepository.findById(UUID.fromString(createReviewDTO.userId())).orElseThrow(() -> new ObjectNotFoundException("User not found"));
            retrievedReview.setUser(retrievedUser);
        }
        return  retrievedReview;
    }
}
