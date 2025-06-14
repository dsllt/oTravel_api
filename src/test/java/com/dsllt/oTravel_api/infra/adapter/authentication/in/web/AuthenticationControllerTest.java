package com.dsllt.oTravel_api.infra.adapter.authentication.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.user.model.UserRole;
import com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model.AuthenticationRequestIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${api.security.token.secret}")
    private String secret;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("should allow to user to login")
    void testLogin() throws Exception {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        var encryptedPassword = new BCryptPasswordEncoder().encode("123456");
        jdbcTemplate.update("""
                INSERT INTO users (id, first_name, last_name, email, image, password, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """, userUuid.toString(), "John", "Doe", "john.doe@email.com", "", encryptedPassword, UserRole.USER.toString(), createdAt.toString());
        var authenticationRequestIn = AuthenticationRequestIn.builder()
                .email("john.doe@email.com")
                .password("123456")
                .build();
        String requestBody = objectMapper.writeValueAsString(authenticationRequestIn);

        // Act and Assert
        mockMvc.perform(
                        post("/api/v1/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userUuid.toString()));
    }
}