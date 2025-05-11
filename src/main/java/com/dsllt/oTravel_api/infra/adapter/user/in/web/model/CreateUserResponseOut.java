package com.dsllt.oTravel_api.infra.adapter.user.in.web.model;

import com.dsllt.oTravel_api.domain.user.model.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateUserResponseOut(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String image,
        UserRole role
) {
}
