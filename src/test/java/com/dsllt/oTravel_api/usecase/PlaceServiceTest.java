//package com.dsllt.oTravel_api.usecase;
//
//import com.dsllt.oTravel_api.domain.model.CustomPage;
//import com.dsllt.oTravel_api.domain.place.model.Place;
//import com.dsllt.oTravel_api.domain.place.model.PlaceFilter;
//import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
//import com.dsllt.oTravel_api.infra.exceptions.ObjectNotFoundException;
//import com.dsllt.oTravel_api.domain.place.service.PlaceService;
//import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
//import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceResponseOut;
//import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
//import com.dsllt.oTravel_api.infra.adapter.place.out.persistence.jpa.PlaceJpaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchException;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.*;
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class PlaceServiceTest {
//    @Mock
//    PlaceJpaRepository placeJpaRepository;
//
//    @InjectMocks
//    PlaceService placeServiceImpl;
//
//    @Captor
//    private ArgumentCaptor<Place> placeArgumentCaptor;
//
//    @BeforeEach
//    public void setup(){
//        this.placeServiceImpl = new PlaceService(placeJpaRepository);
//    }
//
//    @Test
//    @DisplayName("should save place")
//    public void savePlaceTest(){
//        // Arrange
//        CreatePlaceRequestIn createPlaceDTO = new CreatePlaceRequestIn(
//                "The Coffee",
//                "",
//                "",
//                "Rua Fernandes Vieira",
//                "Porto Alegre",
//                "Brasil",
//                -30.0319164,
//                -51.2107576,
//                "the-coffee-bom-fim",
//                "",
//                List.of(PlaceCategory.COFFEE)
//        );
//        String[] categoryArray = createPlaceDTO.category().stream()
//                .map(Enum::name)
//                .toArray(String[]::new);
//        Place repositorySavedPlace = Place.builder()
//                .id(UUID.randomUUID())
//                .name(createPlaceDTO.name())
//                .imageUrl(createPlaceDTO.imageUrl())
//                .description(createPlaceDTO.description())
//                .address(createPlaceDTO.address())
//                .city(createPlaceDTO.city())
//                .country(createPlaceDTO.country())
//                .latitude(createPlaceDTO.latitude())
//                .longitude(createPlaceDTO.longitude())
//                .slug(createPlaceDTO.slug())
//                .phone(createPlaceDTO.phone())
//                .category(categoryArray)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        // Act
//        when(placeJpaRepository.save(any(Place.class))).thenReturn(repositorySavedPlace);
//
//        CreatePlaceResponseOut savedPlaceDTO = placeServiceImpl.save(createPlaceDTO);
//
//        // Assert
//        then(placeJpaRepository).should().save(placeArgumentCaptor.capture());
//        Place savedPlace = placeArgumentCaptor.getValue();
//        assertThat(savedPlace.getName()).isEqualTo(savedPlaceDTO.name());
//        verify(placeJpaRepository, times(1)).save(placeArgumentCaptor.capture());
//    }
//
//    @Test
//    @DisplayName("should throw BusinessException when trying to save place with existing name")
//    public void savePlaceErrorTest(){
//        // Arrange
//        CreatePlaceRequestIn createPlaceDTO = new CreatePlaceRequestIn(
//                "The Coffee",
//                "",
//                "",
//                "Rua Fernandes Vieira",
//                "Porto Alegre",
//                "Brasil",
//                -30.0319164,
//                -51.2107576,
//                "the-coffee-bom-fim",
//                "",
//                List.of(PlaceCategory.COFFEE)
//
//        );
//
//        // Act
//        when(placeJpaRepository.existsByName(anyString())).thenReturn(true);
//        Throwable exception = catchException(() -> placeServiceImpl.save(createPlaceDTO));
//
//        // Assert
//        assertThat(exception).isInstanceOf(BusinessException.class)
//                .hasMessage("Lugar já cadastrado.");
//
//    }
//
//    @Test
//    @DisplayName("should throw BusinessException when trying to save place with existing slug")
//    public void savePlaceErrorTest2(){
//        // Arrange
//        CreatePlaceRequestIn createPlaceDTO = new CreatePlaceRequestIn(
//                "The Coffee",
//                "",
//                "",
//                "Rua Fernandes Vieira",
//                "Porto Alegre",
//                "Brasil",
//                -30.0319164,
//                -51.2107576,
//                "the-coffee-bom-fim",
//                "",
//                List.of(PlaceCategory.COFFEE)
//        );
//
//        // Act
//        when(placeJpaRepository.existsBySlug(anyString())).thenReturn(true);
//        Throwable exception = catchException(() -> placeServiceImpl.save(createPlaceDTO));
//
//        // Assert
//        assertThat(exception).isInstanceOf(BusinessException.class)
//                .hasMessage("Lugar já cadastrado.");
//
//    }
//
//    @Test
//    @DisplayName("should retrieve all places")
//    public void getPlaceTest(){
//        // Arrange
//        Place place = new Place();
//        List<Place> placeList = Arrays.asList(place);
//
//        when(placeJpaRepository.findAll()).thenReturn(placeList);
//
//        // Act
//        List<CreatePlaceResponseOut> retrievedPlaces = placeServiceImpl.get();
//
//        // Assert
//        assertThat(retrievedPlaces).isNotEmpty();
//        assertThat(retrievedPlaces).isInstanceOf(List.class);
//        assertThat(retrievedPlaces.get(0)).isInstanceOf(CreatePlaceResponseOut.class);
//        assertThat(retrievedPlaces).allMatch(Objects::nonNull);
//    }
//
//    @Test
//    @DisplayName("should return a paginated list of places")
//    public void getPlacePaginatedTest(){
//        // Arrange
//        Place place = new Place();
//        List<Place> placeList = Arrays.asList(place);
//        PlaceFilter placeFilter = new PlaceFilter(UUID.randomUUID(),"","","",List.of(),"",0.0);
//        Sort sort = Sort.by(Sort.Direction.ASC, "name");
//        Pageable pageable = PageRequest.of(0, 10, sort);
//        Page<Place> paginatedPlaces = new PageImpl<>(placeList, pageable, placeList.size());
//
//        when(placeJpaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(paginatedPlaces);
//
//        // Act
//        CustomPage<CreatePlaceResponseOut> retrievedPlaces = placeServiceImpl.filter(placeFilter, pageable);
//
//        // Assert
//        assertThat(retrievedPlaces).isNotNull();
//        assertThat(retrievedPlaces.getTotalPages()).isEqualTo(1);
//        assertThat(retrievedPlaces.getTotalElements()).isEqualTo(1);
//        assertThat(retrievedPlaces.getContent()).isNotEmpty();
//        assertThat(retrievedPlaces.getContent().get(0)).isInstanceOf(CreatePlaceResponseOut.class);
//        assertThat(retrievedPlaces.getNumber()).isEqualTo(0);
//        assertThat(retrievedPlaces.getNumberOfElements()).isEqualTo(1);
//        assertThat(retrievedPlaces.isFirst()).isTrue();
//        assertThat(retrievedPlaces.isLast()).isTrue();
//    }
//
//    @Test
//    @DisplayName("should retrieve place when receiving existing id")
//    public void getPlaceByIdTest(){
//        // Arrange
//        UUID placeUuid = UUID.randomUUID();
//        List<PlaceCategory> categoryList = List.of(PlaceCategory.COFFEE);
//        String[] categoryArray = categoryList.stream()
//                .map(Enum::name)
//                .toArray(String[]::new);
//        Place savedPlace = Place.builder()
//                .id(placeUuid)
//                .name("PlaceName Test")
//                .imageUrl("imageURL test")
//                .description("Description test")
//                .address("Address test")
//                .city("City test")
//                .country("Country test")
//                .latitude(-30.10)
//                .longitude(-50.10)
//                .slug("place-test-slug")
//                .phone("00000000000")
//                .category(categoryArray)
//                .createdAt(LocalDateTime.now())
//                .build();
//        when(placeJpaRepository.findById(placeUuid)).thenReturn(Optional.of(savedPlace));
//
//        // Act
//        CreatePlaceResponseOut retrievedPlace = placeServiceImpl.getPlaceById(placeUuid);
//
//        // Assert
//        assertThat(retrievedPlace.id()).isEqualTo(placeUuid);
//        assertThat(retrievedPlace.name()).isEqualTo(savedPlace.getName());
//    }
//
//    @Test
//    @DisplayName("should throw ObjectNotFoundException when receiving non-existing id")
//    public void getPlaceByIdErrorTest(){
//        // Arrange
//        UUID placeUuid = UUID.randomUUID();
//
//        when(placeJpaRepository.findById(placeUuid)).thenThrow(new ObjectNotFoundException("Lugar não encontrado."));
//
//        // Act
//        Throwable exception = catchException(() -> placeServiceImpl.getPlaceById(placeUuid));
//
//        // Assert
//        assertThat(exception).isInstanceOf(ObjectNotFoundException.class)
//                .hasMessage("Lugar não encontrado.");
//    }
//
//    @Test
//    @DisplayName("should allow to update place information when receiving existing id")
//    public void updatePlaceByIdTest(){
//        // Arrange
//        UUID placeUuid = UUID.randomUUID();
//        List<PlaceCategory> categoryList = List.of(PlaceCategory.COFFEE);
//        String[] categoryArray = categoryList.stream()
//                .map(Enum::name)
//                .toArray(String[]::new);
//        Place existingPlace = Place.builder()
//                .id(placeUuid)
//                .name("PlaceName Test")
//                .imageUrl("imageURL test")
//                .description("Description test")
//                .address("Address test")
//                .city("City test")
//                .country("Country test")
//                .latitude(-30.10)
//                .longitude(-50.10)
//                .slug("place-test-slug")
//                .phone("00000000000")
//                .category(categoryArray)
//                .createdAt(LocalDateTime.now())
//                .build();
//        CreatePlaceResponseOut updatePlaceDTO = new CreatePlaceResponseOut( placeUuid,
//                "Updated Name",
//                "",
//                "",
//                existingPlace.getAddress(),
//                existingPlace.getCity(),
//                existingPlace.getCountry(),
//                existingPlace.getLatitude(),
//                existingPlace.getLongitude(),
//                existingPlace.getSlug(),
//                existingPlace.getPhone(),
//                existingPlace.getCategoryList(),
//                existingPlace.getRating());
//        Place updatedPlace = Place.builder()
//                .id(placeUuid)
//                .name(updatePlaceDTO.name())
//                .imageUrl("imageURL test")
//                .description("Description test")
//                .address("Address test")
//                .city("City test")
//                .country("Country test")
//                .latitude(-30.10)
//                .longitude(-50.10)
//                .slug("place-test-slug")
//                .phone("00000000000")
//                .category(categoryArray)
//                .createdAt(LocalDateTime.now())
//                .build();
//        when(placeJpaRepository.findById(placeUuid)).thenReturn(Optional.of(existingPlace));
//        when(placeJpaRepository.save(any(Place.class))).thenReturn(updatedPlace);
//
//        // Act
//        CreatePlaceResponseOut updatedPlaceDTO = placeServiceImpl.updatePlace(placeUuid, updatePlaceDTO);
//
//        // Assert
//        assertThat(updatedPlaceDTO.id()).isEqualTo(placeUuid);
//        assertThat(updatedPlaceDTO.name()).isEqualTo(updatePlaceDTO.name());
//        assertThat(updatedPlaceDTO.imageUrl()).isEqualTo(updatedPlace.getImageUrl());
//        verify(placeJpaRepository, times(1)).save(any(Place.class));
//        verify(placeJpaRepository, times(1)).findById(placeUuid);
//    }
//}