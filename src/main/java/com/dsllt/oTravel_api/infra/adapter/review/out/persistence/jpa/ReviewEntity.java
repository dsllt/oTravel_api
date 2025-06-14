package com.dsllt.oTravel_api.infra.adapter.review.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    private Double rating;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "place_id")
    private UUID placeId;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
