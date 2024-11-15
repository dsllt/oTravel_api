package com.dsllt.oTravel_api.entity.place;

import com.dsllt.oTravel_api.dtos.enums.PlaceCategory;
import com.dsllt.oTravel_api.dtos.place.CreatePlaceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "places")
public class Place {

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
    @Convert(converter = PlaceCategoryArrayConverter.class)
    @Column(name = "category", columnDefinition = "place_category[]")
    private List<PlaceCategory> category;
    private Double rating;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Place(CreatePlaceDTO createPlaceDTO) {
        this.name = createPlaceDTO.name();
        this.imageUrl = createPlaceDTO.imageUrl();
        this.description = createPlaceDTO.description();
        this.address = createPlaceDTO.address();
        this.city = createPlaceDTO.city();
        this.country = createPlaceDTO.country();
        this.latitude = createPlaceDTO.latitude();
        this.longitude = createPlaceDTO.longitude();
        this.slug = createPlaceDTO.slug();
        this.phone = createPlaceDTO.phone();
        this.category = createPlaceDTO.category();
        this.createdAt = LocalDateTime.now();
    }
}
