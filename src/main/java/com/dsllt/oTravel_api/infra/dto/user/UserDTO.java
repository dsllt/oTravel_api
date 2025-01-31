package com.dsllt.oTravel_api.infra.dto.user;

import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.core.entity.user.UserRole;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String image,
        UserRole role

) {
    public UserDTO(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage(), user.getRole());
    }
}
