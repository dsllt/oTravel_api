package com.dsllt.oTravel_api.infra.adapter.user.out.persistence.mapper;

import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toUserEntity(CreateUserRequestIn createUserRequestIn);
}
