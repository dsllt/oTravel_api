package com.dsllt.oTravel_api.infra.adapter.in.web.controller;

import com.dsllt.oTravel_api.domain.service.MenuService;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.EditMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
        URI uri = URI.create("/menu/" + newMenu.id());
        return ResponseEntity.created(uri).body(newMenu);
    }


    @GetMapping("/{placeUuid}")
    public ResponseEntity<List<MenuDTO>> getByPlaceId(@Nonnull @PathVariable UUID placeUuid){
        List<MenuDTO> menus = menuService.getByPlaceId(placeUuid);
        return ResponseEntity.ok().body(menus);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuDTO> update(@Nonnull @PathVariable Long itemId, @Valid @RequestBody EditMenuDTO menu){
        MenuDTO updatedMenu = menuService.update(itemId, menu);
        return ResponseEntity.ok().body(updatedMenu);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@Nonnull @PathVariable Long itemId ){
        menuService.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}
