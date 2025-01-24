package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.infra.dto.LoginResponseDTO;
import com.dsllt.oTravel_api.infra.dto.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.core.entity.user.UserRole;
import com.dsllt.oTravel_api.core.usecase.security.AuthenticationService;
import com.dsllt.oTravel_api.core.usecase.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    AuthenticationService authenticationService;
    @Test
    @DisplayName("should allow to register new user")
    void testRegister() throws  Exception {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        String json = """
                {
                	"email": "john.doe@email.com",
                	"password": "123456",
                	"firstName": "Johnny",
                	"lastName": "Doe"
                }
                """;
        CreateUserDTO mockCreateUserDTO = new CreateUserDTO("John", "Doe", "john.doe@email.com", "", "123456");
        UserDTO mockUserDTO = new UserDTO(userUuid,"John", "Doe", "john.doe@email.com", "", UserRole.USER);
        when(authenticationService.register(any(CreateUserDTO.class))).thenReturn(mockUserDTO);

        // Act
        var response = mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should allow to user to login")
    void testLogin() throws  Exception {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        String json = """
            {
                "email": "john.doe@email.com",
                "password": "123456"
            }
            """;
        AuthenticationDTO mockAuthenticationDTO = new AuthenticationDTO("john.doe@email.com",  "123456");
        LoginResponseDTO mockLoginResponseDTO = new LoginResponseDTO("token", userUuid);
        when(authenticationService.login(mockAuthenticationDTO)).thenReturn(mockLoginResponseDTO);

        // Act
        var response = mockMvc.perform(
                post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }
}