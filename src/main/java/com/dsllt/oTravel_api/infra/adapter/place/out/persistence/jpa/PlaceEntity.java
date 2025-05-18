package com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "places")
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    private String description;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private String slug;
    private String phone;
    private String[] category;
    private Double rating;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}





