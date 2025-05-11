package com.dsllt.oTravel_api.infra.adapter.user.in.web.mapper;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserResponseOutMapper {
    CreateUserResponseOut toCreateUserResponseOut(User user);
}
