package com.dsllt.oTravel_api.domain.menu.port.in;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;

import java.util.List;
import java.util.UUID;

public interface MenuUseCase {
    Menu save(CreateMenuRequestIn createMenuDTO);
    List<Menu> getByPlaceId(UUID placeId);
    Menu update(Long itemId, UpdateMenuRequestIn menu);
    void delete(Long itemId);
}
