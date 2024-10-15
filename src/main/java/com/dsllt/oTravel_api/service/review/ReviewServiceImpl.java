package com.dsllt.oTravel_api.service.review;

import com.dsllt.oTravel_api.dtos.review.CreateReviewDTO;
import com.dsllt.oTravel_api.dtos.review.ReviewDTO;
import com.dsllt.oTravel_api.entity.place.Place;
import com.dsllt.oTravel_api.entity.review.Review;
import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.repository.PlaceRepository;
import com.dsllt.oTravel_api.repository.ReviewRepository;
import com.dsllt.oTravel_api.repository.UserRepository;
import com.dsllt.oTravel_api.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService{
    private ReviewRepository reviewRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, PlaceRepository placeRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review save(CreateReviewDTO createReviewDTO) {
        Place retrievedPlace = placeRepository.findById(UUID.fromString(createReviewDTO.placeId())).orElseThrow(() -> new ObjectNotFoundException("Place not found"));
        User retrievedUser = userRepository.findById(UUID.fromString(createReviewDTO.userId())).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        Review newReview = new Review(createReviewDTO, retrievedUser, retrievedPlace);
        Review savedReview = reviewRepository.save(newReview);
        return savedReview;
    }

    @Override
    public List<Review> get() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(UUID reviewUuid) {
        return reviewRepository.findById(reviewUuid).orElseThrow(() -> new ObjectNotFoundException("Review não encontrada."));
    }

    @Override
    public Review updateReview(UUID reviewUuid, CreateReviewDTO createReviewDTO) {
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
        return reviewRepository.save(retrievedReview);
    }

    @Override
    public void deleteReview(UUID reviewUuid) {
        reviewRepository.deleteById(reviewUuid);
    }
}
