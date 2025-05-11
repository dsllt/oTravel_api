package com.dsllt.oTravel_api.domain.user.port.out;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;

import java.util.UUID;

@FunctionalInterface
public interface UpdateUserPort {

    User updateUser(UUID userUUID, CreateUserRequestIn updateUserRequestIn);
}
