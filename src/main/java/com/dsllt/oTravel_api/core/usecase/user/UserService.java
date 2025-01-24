package com.dsllt.oTravel_api.core.usecase.user;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO save(CreateUserDTO createUserDTO);
    UserDTO getUserById(UUID userUUID);
    UserDTO updateUser(UUID userUUID, CreateUserDTO createUserDTO);
}
