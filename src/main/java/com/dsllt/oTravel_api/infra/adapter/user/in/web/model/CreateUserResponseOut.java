package com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model;

import com.dsllt.oTravel_api.domain.model.user.User;
import com.dsllt.oTravel_api.domain.model.user.UserRole;

import java.util.UUID;

public record CreateUserResponseOut(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String image,
        UserRole role

) {
    public CreateUserResponseOut(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage(), user.getRole());
    }
}
