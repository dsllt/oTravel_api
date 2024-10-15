package com.dsllt.oTravel_api.entity.review;

import com.dsllt.oTravel_api.dtos.review.CreateReviewDTO;
import com.dsllt.oTravel_api.entity.place.Place;
import com.dsllt.oTravel_api.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Review(CreateReviewDTO createReviewDTO, User user, Place place){
        this.description = createReviewDTO.description();
        this.rating = createReviewDTO.rating();
        this.place = place;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }


}
