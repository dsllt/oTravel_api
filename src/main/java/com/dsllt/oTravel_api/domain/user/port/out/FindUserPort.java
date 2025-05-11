package com.dsllt.oTravel_api.domain.user.port.out;

import com.dsllt.oTravel_api.domain.user.model.User;

import java.util.UUID;

@FunctionalInterface
public interface FindUserPort {

    User findUser(UUID userUuid);
}
