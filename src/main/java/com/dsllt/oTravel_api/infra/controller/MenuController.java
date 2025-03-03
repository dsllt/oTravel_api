package com.dsllt.oTravel_api.infra.controller;

import com.dsllt.oTravel_api.core.usecase.MenuService;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuDTO> create(@Valid @RequestBody CreateMenuDTO menu){
        MenuDTO newMenu = menuService.save(menu);
        URI uri = URI.create("/schedule/" + newMenu.id());
        return ResponseEntity.created(uri).body(newMenu);
    }


    @GetMapping("/{placeUuid}")
    public ResponseEntity<MenuDTO[]> getByPlaceId(@Nonnull @PathVariable UUID placeUuid){
        MenuDTO[] menus = menuService.getByPlaceId(placeUuid);
        return ResponseEntity.ok().body(menus);
    }

    @PutMapping
    public ResponseEntity<MenuDTO> update(@Valid @RequestBody MenuDTO menu){
        MenuDTO updatedMenu = menuService.update(menu);
        return ResponseEntity.ok().body(updatedMenu);
    }
}
