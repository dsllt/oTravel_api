package com.dsllt.oTravel_api.infra.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O e-mail do usuário deve ser definido.")
        @Email(message = "O e-mail do usuário deve ser válido.")
        String email,
        @NotBlank (message = "A senha do usuário deve ser definida.")
        String password
) {}
