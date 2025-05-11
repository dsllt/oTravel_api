package com.dsllt.oTravel_api.domain.user.model;

import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;


    public static User createNewUserFromCreateUserDTO(CreateUserRequestIn createUserRequestIn) {
        User user = new User();
        user.setFirstName(createUserRequestIn.firstName());
        user.setLastName(createUserRequestIn.lastName());
        user.setEmail(createUserRequestIn.email());
        user.setPassword(createUserRequestIn.password());
        user.setImage(createUserRequestIn.image());
        user.setRole(UserRole.USER);
        return user;
    }

}
