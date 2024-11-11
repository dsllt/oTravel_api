package com.dsllt.oTravel_api.controller;


import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.entity.user.UserRole;
import com.dsllt.oTravel_api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.security.token.secret}")
    private String secret;

    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("should not allow to access /user directly and throw exception with status code 403")
    void testCreate() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(403, response.getStatus());
    }


    @Test
    @DisplayName("should allow to retrieve user data by id if authorization header is provided")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById() throws Exception{
        // Arrange
        UUID userUuid = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userUuid)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .password("123456")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.findById(userUuid)).thenReturn(Optional.ofNullable(mockUser));

        // Act
        var response = mockMvc.perform(
                get("/api/v1/user/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("should throw error when searching user by invalid id")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById2() throws Exception {
        // Arrange
        String userUuid = "1";

        // Act
        var response = mockMvc.perform(
                get("/api/v1/user/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("should update user")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
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
        User mockUser = User.builder()
                .id(userUuid)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .password("123456")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.findById(userUuid)).thenReturn(Optional.ofNullable(mockUser));
        User mockUpdatedUser = User.builder()
                .id(userUuid)
                .firstName("Johnny")
                .lastName("Doe")
                .email("john.doe@email.com")
                .password("123456")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(mockUpdatedUser);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/user/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }
}