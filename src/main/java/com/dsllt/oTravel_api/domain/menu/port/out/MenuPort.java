package com.dsllt.oTravel_api.domain.menu.port.out;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;

import java.util.List;
import java.util.UUID;

public interface MenuPort {
    Menu create(CreateMenuRequestIn createMenuRequestIn);
    List<Menu> findAllByPlaceId(UUID placeId);
    Menu update(Long itemId, UpdateMenuRequestIn menu);
    void delete(Long itemId);
}
