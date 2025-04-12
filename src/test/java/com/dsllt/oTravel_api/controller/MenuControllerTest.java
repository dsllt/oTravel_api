package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.MenuService;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.EditMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.enums.MenuType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private MenuService menuService;
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
    @DisplayName("Should return status code 400 when trying to create a menu with invalid data")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate() throws Exception {
        // Arrange
        CreateMenuDTO newMenu = new CreateMenuDTO(
                null,
                null,
                null,
                null);
        String requestBody = objectMapper.writeValueAsString(newMenu);
        // Act and assert
        mockMvc.perform(post("/api/v1/menu")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Should return status code 201 when creating a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate2() throws Exception {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place.getId());
        Menu newMenu = new Menu(createMenuDTO, place);
        MenuDTO newMenuDTO = MenuDTO.from(newMenu);
        Mockito.when(menuService.save(Mockito.any(CreateMenuDTO.class))).thenReturn(newMenuDTO);
        String requestBody = objectMapper.writeValueAsString(createMenuDTO);
        // Act and assert
        mockMvc.perform(post("/api/v1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newMenu.getId()))
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).save(Mockito.any(CreateMenuDTO.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create a menu for a place and place that already exist")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testCreate3() throws Exception {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place.getId());
        Mockito.when(menuService.save(Mockito.any(CreateMenuDTO.class)))
                .thenThrow(BusinessException.class);
        String requestBody = objectMapper.writeValueAsString(createMenuDTO);
        // Act and assert
        mockMvc.perform(post("/api/v1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).save(Mockito.any(CreateMenuDTO.class));
    }

    @Test
    @DisplayName("Should list all menus for a place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        Place place = Place.builder().id(placeId).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                placeId);
        MenuDTO menuDTO2 = new MenuDTO(
                1L,
                "Arroz",
                MenuType.FOOD,
                25.00,
                placeId);

        List<MenuDTO> menus = new ArrayList<>();
        menus.add(menuDTO);
        menus.add(menuDTO2);
        String requestResponse = objectMapper.writeValueAsString(menus);
        Mockito.when(menuService.getByPlaceId(placeId))
                .thenReturn(menus);
        // Act and assert
        mockMvc.perform(get("/api/v1/menu/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestResponse))
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).getByPlaceId(placeId);
    }

    @Test
    @DisplayName("Should throw an exception when there is no menu saved for the place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void testGet2() throws Exception {
        // Arrange
        UUID placeId = UUID.randomUUID();
        Mockito.when(menuService.getByPlaceId(placeId))
                .thenThrow(ObjectNotFoundException.class);
        // Act and assert
        mockMvc.perform(get("/api/v1/menu/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).getByPlaceId(placeId);
    }

    @Test
    @DisplayName("Should throw an exception when trying to update unregistered place")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update() throws Exception {
        // Arrange
        EditMenuDTO editMenuDTO = new EditMenuDTO(
                "Batata",
                25.00);
        String requestBody = objectMapper.writeValueAsString(editMenuDTO);
        Mockito.when(menuService.update(1L, editMenuDTO))
                .thenThrow(ObjectNotFoundException.class);
        // Act and assert
        mockMvc.perform(put("/api/v1/menu/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).update(1L, editMenuDTO);
    }

    @Test
    @DisplayName("Should allow to update a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update2() throws Exception {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                place.getId());
        EditMenuDTO editMenu = new EditMenuDTO(
                "Batata",
                25.00);
        String requestBody = objectMapper.writeValueAsString(editMenu);
        Mockito.when(menuService.update(1L, editMenu))
                .thenReturn(menuDTO);
        // Act and assert
        mockMvc.perform(put("/api/v1/menu/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).update(1L, editMenu);
    }

    @Test
    @DisplayName("Should allow to delete a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void delete() throws Exception {
        // Act and assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/menu/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).delete(1L);
    }
}