package com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.mapper;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.jpa.MenuEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    Menu toMenu(MenuEntity menu);
}
