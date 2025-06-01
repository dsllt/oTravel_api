package com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "favorites")
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "place_id")
    private UUID placeId;
    private boolean active;

    public void updateActiveStatus(){
        active = !active;
    }
}
