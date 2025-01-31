package com.dsllt.oTravel_api.controller;


import com.dsllt.oTravel_api.core.usecase.FavoriteService;
import com.dsllt.oTravel_api.core.usecase.PlaceService;
import com.dsllt.oTravel_api.core.usecase.UserService;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
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
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
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
    @DisplayName("should allow to retrieve user data by id if authorization header is provided")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGetById() throws Exception{
        // Arrange
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);

        // Act
        var response = mockMvc.perform(
                get("/api/v1/user/" + testUser.id())
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
        String json = """
                {
                	"email": "john.doe@email.com",
                	"password": "123456",
                	"firstName": "Johnny",
                	"lastName": "Doe"
                }
                """;
        CreateUserDTO createTestUser = new CreateUserDTO("John", "Doe", "jeohndoe@email.com", "","123456");
        UserDTO testUser = userService.save(createTestUser);

        // Act
        var response = mockMvc.perform(
                put("/api/v1/user/" + testUser.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // Assert
        assertEquals(200, response.getStatus());
    }
}