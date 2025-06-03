package com.dsllt.oTravel_api.domain.menu.service;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.domain.menu.port.in.MenuUseCase;
import com.dsllt.oTravel_api.domain.menu.port.out.MenuPort;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class MenuService implements MenuUseCase {

    MenuPort menuPort;

    @Override
    public Menu save(CreateMenuRequestIn createMenuDTO){
        return menuPort.create(createMenuDTO);
    }

    @Override
    public List<Menu> getByPlaceId(UUID placeId){
        return menuPort.findAllByPlaceId(placeId);
    }

    @Override
    public Menu update(Long itemId, UpdateMenuRequestIn menu){
        return menuPort.update(itemId, menu);
    }

    @Override
    public void delete(Long itemId) {
        menuPort.delete(itemId);
    }
}
