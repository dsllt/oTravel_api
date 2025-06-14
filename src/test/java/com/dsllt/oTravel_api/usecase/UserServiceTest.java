//package com.dsllt.oTravel_api.usecase;
//
//import com.dsllt.oTravel_api.domain.user.model.User;
//import com.dsllt.oTravel_api.domain.user.model.UserRole;
//import com.dsllt.oTravel_api.infra.exceptions.BusinessException;
//import com.dsllt.oTravel_api.domain.user.service.UserService;
//import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
//import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
//import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @InjectMocks
//    UserService userService;
//    @Mock
//    UserJpaRepository userJpaRepository;
//
//    @BeforeEach
//    public void setup()
//    {
//        this.userService = new UserService(userJpaRepository);
//    }
//
//    @Test
//    @DisplayName("should save a user")
//    public void saveUserTest() {
//        CreateUserRequestIn createUserRequestIn = new CreateUserRequestIn(
//                "John",
//                "Doe",
//                "doe@gmail.com",
//                "",
//                "teste123"
//        );
//        User repositorySavedUser = new User(UUID.randomUUID(), createUserRequestIn.firstName(), createUserRequestIn.lastName(), createUserRequestIn.email(), createUserRequestIn.image(), createUserRequestIn.password(), UserRole.USER, LocalDateTime.now());
//        when(userJpaRepository.save(any(User.class))).thenReturn(repositorySavedUser);
//
//        CreateUserResponseOut savedUser = userService.save(createUserRequestIn);
//
//        assertThat(savedUser.id()).isNotNull();
//        assertThat(savedUser.firstName()).isEqualTo(createUserRequestIn.firstName());
//        assertThat(savedUser.lastName()).isEqualTo(createUserRequestIn.lastName());
//        assertThat(savedUser.email()).isEqualTo(createUserRequestIn.email());
//        assertThat(savedUser.image()).isEqualTo(createUserRequestIn.image());
//    }
//
//    @Test
//    @DisplayName("should throw exception when trying to register user with existing email")
//    public void saveUserWithExistingEmailTest(){
//        //Arrange
//        CreateUserRequestIn validUser  = new CreateUserRequestIn(
//                "John",
//                "Doe",
//                "doe@gmail.com",
//                "",
//                "teste123"
//        );
//        User repositorySavedUser = new User(UUID.randomUUID(), validUser.firstName(), validUser.lastName(), validUser.email(), validUser.image(), validUser.password(), UserRole.USER, LocalDateTime.now());
//        when(userJpaRepository.existsByEmail(anyString())).thenReturn(true);
//
//        //Act
//        Throwable exception = Assertions.catchException(() -> userService.save(validUser));
//
//        //Assert
//        assertThat(exception).isInstanceOf(BusinessException.class)
//                .hasMessage("E-mail já cadastrado.");
//
//        verify(userJpaRepository, never()).save(repositorySavedUser);
//    }
//
//    @Test
//    @DisplayName("should retrieve user information when receiving existing id")
//    public void getUserByIdTest(){
//        UUID userId = UUID.randomUUID();
//        CreateUserResponseOut createUserResponseOut = new CreateUserResponseOut(
//                userId,
//                "John",
//                "Doe",
//                "doe@gmail.com",
//                "",
//                UserRole.USER
//        );
//        User repositorySavedUser = new User(userId, createUserResponseOut.firstName(), createUserResponseOut.lastName(), createUserResponseOut.email(), createUserResponseOut.image(), "adssasda", UserRole.USER, LocalDateTime.now());
//
//        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(repositorySavedUser));
//
//        CreateUserResponseOut retrievedUser = userService.getUserById(userId);
//
//        assertThat(retrievedUser.id()).isEqualTo(userId);
//    }
//
//    @Test
//    @DisplayName("should allow to update user information when receiving existing id")
//    public void updateUserByIdTest(){
//        // Arrange
//        UUID userUUID = UUID.randomUUID();
//        User retrievedUser = User.builder()
//                .id(userUUID)
//                .firstName("John")
//                .lastName("Doe")
//                .role(UserRole.USER)
//                .email("doe@gmail.com")
//                .createdAt(LocalDateTime.now())
//                .password("123")
//                .image("")
//                .build();
//
//        CreateUserRequestIn userUpdateDTO = new CreateUserRequestIn(
//                "John",
//                "Doe Doe",
//                "doedoe@gmail.com",
//                "",
//                "123"
//        );
//
//        User repositoryUpdatedUser = User.builder()
//                .id(userUUID)
//                .firstName(userUpdateDTO.firstName())
//                .lastName(userUpdateDTO.lastName())
//                .role(UserRole.USER)
//                .email(userUpdateDTO.email())
//                .createdAt(retrievedUser.getCreatedAt())
//                .password("123")
//                .image(userUpdateDTO.image())
//                .build();
//
//        when(userJpaRepository.findById(userUUID)).thenReturn(Optional.of(retrievedUser));
//        when(userJpaRepository.save(any(User.class))).thenReturn(repositoryUpdatedUser);
//
//        // Act
//        CreateUserResponseOut updatedUser = userService.updateUser(userUUID, userUpdateDTO);
//
//        // Assert
//        assertThat(updatedUser.id()).isEqualTo(userUUID);
//        assertThat(updatedUser.firstName()).isEqualTo(userUpdateDTO.firstName());
//        assertThat(updatedUser.lastName()).isEqualTo(userUpdateDTO.lastName());
//        assertThat(updatedUser.email()).isEqualTo(userUpdateDTO.email());
//
//        // Verify that save was called with the correct user
//        verify(userJpaRepository, times(1)).save(any(User.class));
//        // Verify that findById was called
//        verify(userJpaRepository, times(1)).findById(userUUID);
//    }
//}