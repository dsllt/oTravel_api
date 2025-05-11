package com.dsllt.oTravel_api.infra.adapter.user.out.persistence;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.domain.user.model.UserRole;
import com.dsllt.oTravel_api.domain.user.port.out.CreateUserPort;
import com.dsllt.oTravel_api.domain.user.port.out.FindUserPort;
import com.dsllt.oTravel_api.domain.user.port.out.UpdateUserPort;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.mapper.UserMapper;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserEntity;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.mapper.UserEntityMapper;
import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
@AllArgsConstructor
public class UserRepository implements CreateUserPort, FindUserPort, UpdateUserPort {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final UserMapper userMapper;

    @Override
    public User create(CreateUserRequestIn createUserRequestIn) {
        if (userJpaRepository.existsByEmail(createUserRequestIn.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        var encryptedPassword = new BCryptPasswordEncoder().encode(createUserRequestIn.password());
        UserEntity user = userEntityMapper.toUserEntity(createUserRequestIn);
        user.setRole(UserRole.USER);
        user.setPassword(encryptedPassword);
        UserEntity savedUser = userJpaRepository.save(user);
        return userMapper.toUser(savedUser);
    }

    @Override
    public User findUser(UUID userUuid) {
        UserEntity user = userJpaRepository.findById(userUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
        return userMapper.toUser(user);
    }

    @Override
    public User updateUser(UUID userUuid, CreateUserRequestIn updateUserRequestIn) {
        UserEntity retrievedUser =  userJpaRepository.findById(userUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
        UserEntity updatedUser = updateUser(retrievedUser, updateUserRequestIn);
        UserEntity savedUser = userJpaRepository.save(updatedUser);
        return userMapper.toUser(savedUser);
    }

    private UserEntity updateUser(UserEntity retrievedUser, CreateUserRequestIn updateUserRequestIn) {
        if (updateUserRequestIn.firstName() != null && !retrievedUser.getFirstName().isEmpty()) {
            retrievedUser.setFirstName(updateUserRequestIn.firstName());
        }
        if (updateUserRequestIn.lastName() != null && !retrievedUser.getLastName().isEmpty()) {
            retrievedUser.setLastName(updateUserRequestIn.lastName());
        }
        if (updateUserRequestIn.email() != null && !retrievedUser.getEmail().isEmpty()) {
            retrievedUser.setEmail(updateUserRequestIn.email());
        }
        if (updateUserRequestIn.image() != null && !retrievedUser.getImage().isEmpty()) {
            retrievedUser.setImage(updateUserRequestIn.image());
        }

        return retrievedUser;
    }
}
