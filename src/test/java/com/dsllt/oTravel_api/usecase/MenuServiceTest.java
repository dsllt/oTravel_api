//package com.dsllt.oTravel_api.usecase;
//
//import com.dsllt.oTravel_api.domain.menu.model.Menu;
//import com.dsllt.oTravel_api.domain.place.model.Place;
//import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
//import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
//import com.dsllt.oTravel_api.domain.menu.service.MenuService;
//import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.CreateMenuDTO;
//import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.EditMenuDTO;
//import com.dsllt.oTravel_api.infra.adapter.menu.in.web.model.MenuDTO;
//import com.dsllt.oTravel_api.domain.menu.model.MenuType;
//import com.dsllt.oTravel_api.infra.adapter.menu.out.persistence.jpa.MenuJpaRepository;
//import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchException;
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//
//    @Mock
//    MenuJpaRepository menuJpaRepository;
//    @Mock
//    PlaceJpaRepository placeJpaRepository;
//    @InjectMocks
//    MenuService menuService;
//
//    @Test
//    @DisplayName("Should throw exception when menu is already registered")
//    public void save() {
//        // Arrange
//        Place place = Place.builder().id(UUID.randomUUID()).build();
//        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
//                "Batata",
//                MenuType.FOOD,
//                25.00,
//                place.getId());
//        Mockito.when(menuJpaRepository.existsByNameAndPlaceId(Mockito.any(String.class), Mockito.any(UUID.class)))
//                .thenReturn(true);
//        // Act
//        Throwable exception = catchException(() -> menuService.save(createMenuDTO));
//        // Assert
//        assertThat(exception).isInstanceOf(BusinessException.class);
//    }
//
//    @Test
//    @DisplayName("Should save a menu")
//    public void save2() {
//        // Arrange
//        Place place = Place.builder().id(UUID.randomUUID()).build();
//        CreateMenuDTO createMenuDTO = new CreateMenuDTO(
//                "Batata",
//                MenuType.FOOD,
//                25.00,
//                place.getId());
//        Menu persistedMenu = Menu.builder().build();;
//        Mockito.when(menuJpaRepository.save(Mockito.any(Menu.class)))
//                .thenReturn(persistedMenu);
//        // Act
//        MenuDTO savedMenu = menuService.save(createMenuDTO);
//        // Assert
//        Assertions.assertNotNull(savedMenu);
//        Assertions.assertEquals(createMenuDTO.name(), savedMenu.name());
//        Assertions.assertEquals(createMenuDTO.type(), savedMenu.type());
//        Assertions.assertEquals(createMenuDTO.price(), savedMenu.price());
//    }
//
//    @Test
//    @DisplayName("Should throw exception when no data is found")
//    public void get() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Mockito.when(menuJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(false);
//        // Act
//        Throwable exception = catchException(() -> menuService.getByPlaceId(placeUUID));
//        // Assert
//        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
//                .hasMessage("Menus não encontrados para este local.");
//    }
//
//    @Test
//    @DisplayName("Should retrieve all menus for a place")
//    public void get2() {
//        // Arrange
//        UUID placeUuid = UUID.randomUUID();
//        Menu persistedMenu = new Menu(
//                1L,
//                "Batata",
//                MenuType.FOOD,
//                25.00,
//                placeUuid,
//                ZonedDateTime.now(),
//                ZonedDateTime.now());
//        Menu persistedMenu2 = new Menu(
//                2L,
//                "Arroz",
//                MenuType.FOOD,
//                25.00,
//                placeUuid,
//                ZonedDateTime.now(),
//                ZonedDateTime.now());
//        List<Menu> persistedMenus = new ArrayList<>();
//        persistedMenus.add(persistedMenu);
//        persistedMenus.add(persistedMenu2);
//        List<MenuDTO> persistedMenusDTO = persistedMenus.stream()
//                .map(menu -> MenuDTO.builder().build()).toList();
//        Mockito.when(menuJpaRepository.existsByPlaceId(placeUuid))
//                .thenReturn(true);
//        Mockito.when(menuJpaRepository.findAllByPlaceId(placeUuid))
//                .thenReturn(persistedMenus);
//        // Act
//        List<MenuDTO> savedMenus = menuService.getByPlaceId(placeUuid);
//        // Assert
//        Assertions.assertNotNull(savedMenus);
//        Assertions.assertEquals(savedMenus.size(), persistedMenusDTO.size());
//        Assertions.assertEquals(savedMenus, persistedMenusDTO);
//    }
//
//    @Test
//    @DisplayName("Should throw exception when no data is found")
//    public void update() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        EditMenuDTO editMenuDTO = new EditMenuDTO(
//                "Batata",
//                25.00);
//        Mockito.when(placeJpaRepository.findById(placeUUID))
//                .thenReturn(Optional.ofNullable(place));
//        Mockito.when(menuJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(false);
//        // Act
//        Throwable exception = catchException(() -> menuService.update(1L, editMenuDTO));
//        // Assert
//        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
//                .hasMessage("Menus não encontrados para este local.");
//    }
//
//    @Test
//    @DisplayName("Should update places schedule")
//    public void update2() {
//        // Arrange
//        UUID placeUUID = UUID.randomUUID();
//        Place place = Place.builder().id(placeUUID).build();
//        MenuDTO menuDTO = new MenuDTO(
//                1L,
//                "Batata",
//                MenuType.FOOD,
//                25.00,
//                placeUUID);
//        EditMenuDTO editMenuDTO = new EditMenuDTO(
//                "Batata",
//                25.00);
//        Menu persistedMenu = Menu.builder().build();
//        Mockito.when(placeJpaRepository.findById(placeUUID))
//                .thenReturn(Optional.ofNullable(place));
//        Mockito.when(menuJpaRepository.existsByPlaceId(placeUUID))
//                .thenReturn(true);
//        Mockito.when(menuJpaRepository.save(Mockito.any(Menu.class)))
//                .thenReturn(persistedMenu);
//        // Act
//        MenuDTO updatedMenu = menuService.update(1L, editMenuDTO);
//        // Assert
//        Assertions.assertNotNull(updatedMenu);
//        Assertions.assertEquals(1L, updatedMenu.id());
//        Assertions.assertEquals(editMenuDTO.name(), updatedMenu.name());
//    }
//}