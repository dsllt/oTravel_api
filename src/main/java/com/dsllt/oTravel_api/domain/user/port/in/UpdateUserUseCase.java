package com.dsllt.oTravel_api.domain.user.port.in;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;

import java.util.UUID;

@FunctionalInterface
public interface UpdateUserUseCase {
    User updateUser(UUID userUUID, CreateUserRequestIn updateUserRequestIn);
}
