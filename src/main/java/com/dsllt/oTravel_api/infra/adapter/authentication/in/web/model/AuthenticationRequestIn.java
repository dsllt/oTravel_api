package com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequestIn(
        @NotBlank(message = "O e-mail do usuário deve ser definido.")
        @Email(message = "O e-mail do usuário deve ser válido.")
        String email,
        @NotBlank (message = "A senha do usuário deve ser definida.")
        String password
) {}
