package com.dsllt.oTravel_api.core.entity.menu;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.enums.MenuType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MenuType type;
    @Column(name = "price")
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Menu(CreateMenuDTO createMenuDTO){
        this.name = createMenuDTO.name();
        this.type = createMenuDTO.type();
        this.price = createMenuDTO.price();
        this.place = createMenuDTO.place();
    }

    public Menu(MenuDTO menuDTO){
        this.id = menuDTO.id();
        this.name = menuDTO.name();
        this.type = menuDTO.type();
        this.price = menuDTO.price();
        this.place = menuDTO.place();
        this.updatedAt = ZonedDateTime.now();
    }
}
