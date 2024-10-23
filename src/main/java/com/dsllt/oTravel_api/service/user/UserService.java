package com.dsllt.oTravel_api.service.user;
import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO save(CreateUserDTO createUserDTO);
    UserDTO getUserById(UUID userUUID);
    UserDTO updateUser(UUID userUUID, CreateUserDTO createUserDTO);
}
