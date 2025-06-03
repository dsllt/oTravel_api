package com.dsllt.oTravel_api.infra.adapter.menu.in.web;

import com.dsllt.oTravel_api.app.OTravelApiApplication;
import com.dsllt.oTravel_api.domain.menu.model.MenuType;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuRequestIn;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.MenuResponseOut;
import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.UpdateMenuRequestIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OTravelApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    private UUID placeUuid;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM favorites");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM schedules");
    }

    @BeforeEach
    void setup() {
        placeUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        jdbcTemplate.execute("""
                    INSERT INTO places (
                        id, name, image_url, description, address,
                        city, country, latitude, longitude, slug,
                        phone, category, rating, created_at
                    ) VALUES (
                        '550e8400-e29b-41d4-a716-446655440000',
                        'Test Place',
                        'https://example.com/image.jpg',
                        'A beautiful test place',
                        '123 Test St',
                        'Testville',
                        'Testland',
                        10.123456,
                        20.654321,
                        'test-place',
                        '+1234567890',
                        'COFFEE',
                        4.5,
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    @Test
    @DisplayName("Should return status code 400 when trying to create a menu with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        var newMenu = new CreateMenuRequestIn(
                null,
                null,
                null,
                null);
        // Act and assert
        performCreateMenuRequest(newMenu)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status code 201 when creating a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        var newMenu = new CreateMenuRequestIn(
                "Batata",
                MenuType.FOOD,
                25.00,
                placeUuid);

        // Act and assert
        performCreateMenuRequest(newMenu)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Should throw exception when trying to create a menu for a place and that already exist")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate3() throws Exception {
        // Arrange
        var newMenu = new CreateMenuRequestIn(
                "Batata",
                MenuType.FOOD,
                25.00,
                placeUuid);

        // Act and assert
        performCreateMenuRequest(newMenu)
                .andExpect(status().isCreated());
        performCreateMenuRequest(newMenu)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should list all menus for a place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet() throws Exception {
        // Arrange
        createMenusOnDatabase();
        var menuList = getMenuResponseOut();
        String responseBody = objectMapper.writeValueAsString(menuList);

        // Act and assert
        performGetMenusRequest(placeUuid)
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    @DisplayName("Should throw an exception when there is no menu saved for the place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet2() throws Exception {
        // Arrange

        // Act and assert
        performGetMenusRequest(placeUuid)
                .andExpect(status().isNotFound());
    }

        @Test
    @DisplayName("Should throw an exception when trying to update unregistered menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update() throws Exception {
        // Arrange
            var updateMenuRequestIn = UpdateMenuRequestIn.builder()
                    .name("Refrigerante")
                    .price(10.0)
                    .build();
        // Act and assert
            performPutMenuRequest(1L, updateMenuRequestIn)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should allow to update a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update2() throws Exception {
        // Arrange
        createMenusOnDatabase();
        var updateMenuRequestIn = new UpdateMenuRequestIn(
                "Batata Editada",
                30.00);
        var responseOut = MenuResponseOut.builder()
                .id(1L)
                .name("Batata Editada")
                .type(MenuType.FOOD)
                .price(30.0)
                .placeId(placeUuid)
                .build();
        String responseBody = objectMapper.writeValueAsString(responseOut);

        // Act and assert
        performPutMenuRequest(1L, updateMenuRequestIn)
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    @DisplayName("Should allow to delete a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void delete() throws Exception {
        // Arrange
        createMenusOnDatabase();

        // Act and assert
        performDeleteMenuRequest(1L)
                .andExpect(status().isNoContent());
    }

    private ResultActions performCreateMenuRequest(Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post("/api/v1/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private ResultActions performGetMenusRequest(UUID placeId) throws Exception {
        return mockMvc.perform(get("/api/v1/menu/" + placeId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performPutMenuRequest(Long menuId, Object body) throws Exception {
        String requestBody = objectMapper.writeValueAsString(body);
        return mockMvc.perform(put("/api/v1/menu/" + menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private ResultActions performDeleteMenuRequest(Long menuId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/menu/" + menuId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void createMenusOnDatabase() {
        jdbcTemplate.execute("""
                    INSERT INTO menus (
                        id, name, type, price, place_id, created_at, updated_at
                    ) VALUES (
                        1,
                        'Batata',
                        'FOOD',
                        25.00,
                        '550e8400-e29b-41d4-a716-446655440000',
                        CURRENT_TIMESTAMP,
                        CURRENT_TIMESTAMP
                    );
                """);
        jdbcTemplate.execute("""
                    INSERT INTO menus (
                        id, name, type, price, place_id, created_at, updated_at
                    ) VALUES (
                        2,
                        'Arroz',
                        'FOOD',
                        25.00,
                        '550e8400-e29b-41d4-a716-446655440000',
                        CURRENT_TIMESTAMP,
                        CURRENT_TIMESTAMP
                    );
                """);
    }

    private List<MenuResponseOut> getMenuResponseOut() {
        var menuResponseOut = new MenuResponseOut(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                placeUuid);
        var menuResponseOut2 = new MenuResponseOut(
                2L,
                "Arroz",
                MenuType.FOOD,
                25.00,
                placeUuid);
        return List.of(menuResponseOut, menuResponseOut2);
    }
}