package com.dsllt.oTravel_api.infra.adapter.menu.out.persistence;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.domain.menu.port.out.MenuPort;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.jpa.MenuJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.mapper.MenuEntityMapper;
import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.mapper.MenuMapper;
import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class MenuRepository implements MenuPort {

    MenuJpaRepository menuJpaRepository;
    MenuEntityMapper menuEntityMapper;
    MenuMapper menuMapper;

    @Override
    public Menu create(CreateMenuRequestIn createMenuRequestIn) {
        if (menuJpaRepository.existsByNameAndPlaceId(createMenuRequestIn.name(), createMenuRequestIn.placeId())) {
            var message = String.format("Item %s já cadastrado para %s.", createMenuRequestIn.name(), createMenuRequestIn.placeId());
            throw new BusinessException(message);
        }

        var menu = menuEntityMapper.toMenuEntity(createMenuRequestIn);
        var savedMenu = menuJpaRepository.save(menu);
        return menuMapper.toMenu(savedMenu);
    }

    @Override
    public List<Menu> findAllByPlaceId(UUID placeId) {
        if (!menuJpaRepository.existsByPlaceId(placeId)) {
            throw new ObjectNotFoundException("Menus não encontrados para este local.");
        }
        var menus = menuJpaRepository.findAllByPlaceId(placeId);
        return menus.stream().map(menuMapper::toMenu).toList();
    }

    @Override
    public Menu update(Long itemId, UpdateMenuRequestIn menu) {
        var retrievedMenu = menuJpaRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Menu não encontrado."));
        var updatedMenu = retrievedMenu.toBuilder()
                .name(menu.name())
                .price(menu.price())
                .build();
        var savedMenu = menuJpaRepository.save(updatedMenu);
        return menuMapper.toMenu(savedMenu);
    }

    @Override
    public void delete(Long itemId) {
        menuJpaRepository.deleteById(itemId);
    }
}
