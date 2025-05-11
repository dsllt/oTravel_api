package com.dsllt.oTravel_api.infra.adapter.user.in.web.mapper;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserEntity userEntity);
}
