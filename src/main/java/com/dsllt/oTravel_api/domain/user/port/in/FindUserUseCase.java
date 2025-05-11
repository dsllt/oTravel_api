package com.dsllt.oTravel_api.domain.user.port.in;

import com.dsllt.oTravel_api.domain.user.model.User;

import java.util.UUID;

@FunctionalInterface
public interface FindUserUseCase {

    User findUser(UUID userUuid);
}
