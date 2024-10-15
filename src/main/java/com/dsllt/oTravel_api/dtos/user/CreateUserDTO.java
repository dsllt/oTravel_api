package com.dsllt.oTravel_api.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreateUserDTO(
        @NotBlank(message = "O nome deve ser definido.") String firstName,
        @NotBlank(message = "O sobrenome deve ser definido.") String lastName,
        @NotBlank(message = "O email deve ser definido.") @Email(message = "Digite um e-mail válido") String email,
        String image,
        @NotBlank(message = "Uma senha deve ser definida.") String password

) {
}
