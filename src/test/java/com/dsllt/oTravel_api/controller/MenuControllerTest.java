package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.MenuService;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.enums.MenuType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
        Place place = Place.builder().build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place);
        Menu newMenu = new Menu(createMenuDTO);
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
        Place place = Place.builder().build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place);
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
                place);
        MenuDTO menuDTO2 = new MenuDTO(
                1L,
                "Arroz",
                MenuType.FOOD,
                25.00,
                place);
        MenuDTO[] menus = {menuDTO,menuDTO2};
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
        Place place = Place.builder().id(UUID.randomUUID()).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                place);
        String requestBody = objectMapper.writeValueAsString(menuDTO);
        Mockito.when(menuService.update(Mockito.any(MenuDTO.class)))
                .thenThrow(ObjectNotFoundException.class);
        // Act and assert
        mockMvc.perform(put("/api/v1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).update(Mockito.any(MenuDTO.class));
    }

    @Test
    @DisplayName("Should allow to update a menu")
    @WithMockUser(value = "john", authorities = "ROLE_USER")
    void update2() throws Exception {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                place);
        String requestBody = objectMapper.writeValueAsString(menuDTO);
        Mockito.when(menuService.update(menuDTO))
                .thenReturn(menuDTO);
        // Act and assert
        mockMvc.perform(put("/api/v1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
        Mockito.verify(menuService, Mockito.times(1)).update(Mockito.any(MenuDTO.class));
    }
}