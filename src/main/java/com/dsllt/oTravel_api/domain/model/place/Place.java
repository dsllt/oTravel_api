package com.dsllt.oTravel_api.core.entity.place;

import com.dsllt.oTravel_api.infra.enums.PlaceCategory;
import com.dsllt.oTravel_api.infra.dto.place.CreatePlaceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private String[] category;
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
        this.createdAt = LocalDateTime.now();
        this.setCategoryList(createPlaceDTO.category());
    }

    public List<PlaceCategory> getCategoryList() {
        return category != null
                ? Arrays.stream(category).map(PlaceCategory::valueOf).collect(Collectors.toList())
                : List.of();
    }

    public void setCategoryList(List<PlaceCategory> categories) {
        this.category = categories != null
                ? categories.stream().map(Enum::name).toArray(String[]::new)
                : new String[]{};
    }
}
