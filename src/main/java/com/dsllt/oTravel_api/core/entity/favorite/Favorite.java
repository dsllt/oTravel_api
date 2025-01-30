package com.dsllt.oTravel_api.core.entity.favorite;

import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "place_id")
    private UUID placeId;
    private boolean active;

    public Favorite(CreateFavoriteDTO createFavoriteDTO){
        this.userId = createFavoriteDTO.userId();
        this.placeId = createFavoriteDTO.placeId();
        this.active = true;
    }
}
