package com.dsllt.oTravel_api.infra.adapter.menu.in.web;

import com.dsllt.oTravel_api.domain.menu.port.in.MenuUseCase;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.mapper.MenuResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.MenuResponseOut;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuUseCase menuUseCase;
    private final MenuResponseOutMapper menuResponseOutMapper;

    @PostMapping
    public ResponseEntity<MenuResponseOut> create(@Valid @RequestBody CreateMenuRequestIn menu){
        var newMenu = menuUseCase.save(menu);
        URI uri = URI.create("/menu/" + newMenu.id());
        var response = menuResponseOutMapper.toMenuResponseOut(newMenu);
        return ResponseEntity.created(uri).body(response);
    }


    @GetMapping("/{placeUuid}")
    public ResponseEntity<List<MenuResponseOut>> getByPlaceId(@Nonnull @PathVariable UUID placeUuid){
        var menus = menuUseCase.getByPlaceId(placeUuid);
        var response = menus.stream()
                .map(menuResponseOutMapper::toMenuResponseOut)
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuResponseOut> update(@Nonnull @PathVariable Long itemId, @Valid @RequestBody UpdateMenuRequestIn menu){
        var updatedMenu = menuUseCase.update(itemId, menu);
        var response = menuResponseOutMapper.toMenuResponseOut(updatedMenu);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@Nonnull @PathVariable Long itemId ){
        menuUseCase.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}
