package com.dsllt.oTravel_api.infra.adapter.menu.in.web.mapper;

import com.dsllt.oTravel_api.domain.menu.model.Menu;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.MenuResponseOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuResponseOutMapper {

    MenuResponseOut toMenuResponseOut(Menu menu);
}
