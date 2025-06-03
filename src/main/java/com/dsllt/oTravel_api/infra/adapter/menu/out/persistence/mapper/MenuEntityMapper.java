package com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.mapper;

import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.jpa.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MenuEntity toMenuEntity(CreateMenuRequestIn createMenuRequestIn);
}
