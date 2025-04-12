package com.dsllt.oTravel_api.usecase;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.usecase.MenuService;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.enums.MenuType;
import com.dsllt.oTravel_api.infra.repository.MenuRepository;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    PlaceRepository placeRepository;
    @InjectMocks
    MenuService menuService;

    @Test
    @DisplayName("Should throw exception when menu is already registered")
    public void save() {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place.getId());
        Mockito.when(menuRepository.existsByNameAndPlaceId(Mockito.any(String.class), Mockito.any(UUID.class)))
                .thenReturn(true);
        // Act
        Throwable exception = catchException(() -> menuService.save(createMenuDTO));
        // Assert
        assertThat(exception).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Should save a menu")
    public void save2() {
        // Arrange
        Place place = Place.builder().id(UUID.randomUUID()).build();
        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
                "Batata",
                MenuType.FOOD,
                25.00,
                place.getId());
        Menu persistedMenu = new Menu(createMenuDTO, place);
        Mockito.when(menuRepository.save(Mockito.any(Menu.class)))
                .thenReturn(persistedMenu);
        // Act
        MenuDTO savedMenu = menuService.save(createMenuDTO);
        // Assert
        Assertions.assertNotNull(savedMenu);
        Assertions.assertEquals(createMenuDTO.name(), savedMenu.name());
        Assertions.assertEquals(createMenuDTO.type(), savedMenu.type());
        Assertions.assertEquals(createMenuDTO.price(), savedMenu.price());
    }

    @Test
    @DisplayName("Should throw exception when no data is found")
    public void get() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Mockito.when(menuRepository.existsByPlaceId(placeUUID))
                .thenReturn(false);
        // Act
        Throwable exception = catchException(() -> menuService.getByPlaceId(placeUUID));
        // Assert
        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Menus não encontrados para este local.");
    }

    @Test
    @DisplayName("Should retrieve all menus for a place")
    public void get2() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        Menu persistedMenu = new Menu(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                place,
                ZonedDateTime.now(),
                ZonedDateTime.now());
        Menu persistedMenu2 = new Menu(
                2L,
                "Arroz",
                MenuType.FOOD,
                25.00,
                place,
                ZonedDateTime.now(),
                ZonedDateTime.now());
        List<Menu> persistedMenus = new ArrayList<>();
        persistedMenus.add(persistedMenu);
        persistedMenus.add(persistedMenu2);
        List<MenuDTO> persistedMenusDTO = List.of(persistedMenus.stream()
                .map(MenuDTO::from)
                .toArray(MenuDTO[]::new));
        Mockito.when(menuRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
        Mockito.when(menuRepository.findAllByPlaceId(placeUUID))
                .thenReturn(persistedMenus);
        // Act
        List<MenuDTO> savedMenus = menuService.getByPlaceId(placeUUID);
        // Assert
        Assertions.assertNotNull(savedMenus);
        Assertions.assertEquals(savedMenus.size(), persistedMenusDTO.size());
        Assertions.assertEquals(savedMenus, persistedMenusDTO);
    }

    @Test
    @DisplayName("Should throw exception when no data is found")
    public void update() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                placeUUID);
        Mockito.when(placeRepository.findById(placeUUID))
                .thenReturn(Optional.ofNullable(place));
        Mockito.when(menuRepository.existsByPlaceId(placeUUID))
                .thenReturn(false);
        // Act
        Throwable exception = catchException(() -> menuService.update(menuDTO));
        // Assert
        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Menus não encontrados para este local.");
    }

    @Test
    @DisplayName("Should update places schedule")
    public void update2() {
        // Arrange
        UUID placeUUID = UUID.randomUUID();
        Place place = Place.builder().id(placeUUID).build();
        MenuDTO menuDTO = new MenuDTO(
                1L,
                "Batata",
                MenuType.FOOD,
                25.00,
                placeUUID);
        Menu persistedMenu = new Menu(menuDTO, place);
        Mockito.when(placeRepository.findById(placeUUID))
                .thenReturn(Optional.ofNullable(place));
        Mockito.when(menuRepository.existsByPlaceId(placeUUID))
                .thenReturn(true);
        Mockito.when(menuRepository.save(Mockito.any(Menu.class)))
                .thenReturn(persistedMenu);
        // Act
        MenuDTO updatedMenu = menuService.update(menuDTO);
        // Assert
        Assertions.assertNotNull(updatedMenu);
        Assertions.assertEquals(updatedMenu.id(),menuDTO.id());
        Assertions.assertEquals(updatedMenu.name(),menuDTO.name());
    }
}