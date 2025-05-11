package com.dsllt.oTravel_api.domain.user.port.out;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;

@FunctionalInterface
public interface CreateUserPort {

    User create(CreateUserRequestIn createUserRequestIn);
}
