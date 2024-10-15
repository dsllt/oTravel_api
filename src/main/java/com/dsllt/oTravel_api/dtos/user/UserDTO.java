package com.dsllt.oTravel_api.dtos.user;

import com.dsllt.oTravel_api.entity.user.UserRole;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String image,
        UserRole role

) {
}
