package com.dsllt.oTravel_api.domain.user.service;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.domain.user.port.in.CreateUserUseCase;
import com.dsllt.oTravel_api.domain.user.port.in.FindUserUseCase;
import com.dsllt.oTravel_api.domain.user.port.in.UpdateUserUseCase;
import com.dsllt.oTravel_api.domain.user.port.out.CreateUserPort;
import com.dsllt.oTravel_api.domain.user.port.out.FindUserPort;
import com.dsllt.oTravel_api.domain.user.port.out.UpdateUserPort;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements CreateUserUseCase, FindUserUseCase, UpdateUserUseCase {

    private final CreateUserPort createUserPort;
    private final FindUserPort findUserPort;
    private final UpdateUserPort updateUserPort;

    @Override
    public User create(CreateUserRequestIn createUserRequestIn) {
        return createUserPort.create(createUserRequestIn);
    }

    @Override
    public User findUser(UUID userUuid) {
        return findUserPort.findUser(userUuid);
    }

    @Override
    public User updateUser(UUID userUuid, CreateUserRequestIn updateUserRequestIn) {
        return updateUserPort.updateUser(userUuid,updateUserRequestIn);
    }
}
