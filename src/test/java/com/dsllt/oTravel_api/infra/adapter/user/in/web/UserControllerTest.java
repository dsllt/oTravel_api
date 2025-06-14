package com.dsllt.oTravel_api.infra.adapter.user.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.user.model.UserRole;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${api.security.token.secret}")
    private String secret;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM schedules");
    }

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
    @DisplayName("should allow to create new user")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var createUserRequestIn = CreateUserRequestIn.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .image("")
                .password("123456")
                .build();
        String requestBody = objectMapper.writeValueAsString(createUserRequestIn);
        // Act and Assert
        mockMvc.perform(
                        post("/api/v1/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@email.com"))
                .andExpect(jsonPath("$.image").value(""))
                .andExpect(jsonPath("$.role").value("USER"))
                .andDo(print());

    }

    @Test
    @DisplayName("should allow to retrieve user data by id if authorization header is provided")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        CreateUserResponseOut createUserResponseOut = CreateUserResponseOut.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .image("")
                .role(UserRole.USER)
                .build();
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update("""
                INSERT INTO users (id, first_name, last_name, email, image, password, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, userId.toString(), "John", "Doe", "john.doe@email.com", "", "123456", UserRole.USER.toString(), createdAt.toString());
        var responseBody = objectMapper.writeValueAsString(createUserResponseOut);
        // Act and Assert
        mockMvc.perform(
                        get("/api/v1/user/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    @DisplayName("should throw error when searching user by invalid id")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById2() throws Exception {
        // Arrange
        String userUuid = "1";

        // Act and Assert
        mockMvc.perform(
                        get("/api/v1/user/" + userUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("should update user")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testUpdate() throws Exception {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        var updateUserRequestIn = CreateUserRequestIn.builder()
                .firstName("John")
                .lastName("Doe Doe")
                .email("johndoe@email.com")
                .image("")
                .password("123456")
                .build();
        var updateUserResponseOut = CreateUserResponseOut.builder()
                .id(userUuid)
                .firstName("John")
                .lastName("Doe Doe")
                .email("johndoe@email.com")
                .image("")
                .role(UserRole.USER)
                .build();
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update("""
                INSERT INTO users (id, first_name, last_name, email, image, password, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, userUuid.toString(), "John", "Doe", "john.doe@email.com", "", "123456", UserRole.USER.toString(), createdAt.toString());
        var requestBody = objectMapper.writeValueAsString(updateUserRequestIn);
        var responseBody = objectMapper.writeValueAsString(updateUserResponseOut);

        // Act and Assert
        var response = mockMvc.perform(
                put("/api/v1/user/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }
}