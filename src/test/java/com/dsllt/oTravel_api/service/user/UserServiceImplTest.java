package com.dsllt.oTravel_api.service.user;

import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;
import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.entity.user.UserRole;
import com.dsllt.oTravel_api.repository.UserRepository;
import com.dsllt.oTravel_api.service.exceptions.EmailAlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setup()
    {
        this.userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("should save a user")
    public void saveUserTest() {
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "John",
                "Doe",
                "doe@gmail.com",
                "",
                "teste123"
        );
        User repositorySavedUser = new User(UUID.randomUUID(), createUserDTO.firstName(), createUserDTO.lastName(), createUserDTO.email(), createUserDTO.image(), createUserDTO.password(), UserRole.USER, LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(repositorySavedUser);

        UserDTO savedUser = userService.save(createUserDTO);

        assertThat(savedUser.id()).isNotNull();
        assertThat(savedUser.firstName()).isEqualTo(createUserDTO.firstName());
        assertThat(savedUser.lastName()).isEqualTo(createUserDTO.lastName());
        assertThat(savedUser.email()).isEqualTo(createUserDTO.email());
        assertThat(savedUser.image()).isEqualTo(createUserDTO.image());
    }

    @Test
    @DisplayName("should throw exception when trying to register user with existing email")
    public void saveUserWithExistingEmailTest(){
        //Arrange
        CreateUserDTO validUser  = new CreateUserDTO(
                "John",
                "Doe",
                "doe@gmail.com",
                "",
                "teste123"
        );
        User repositorySavedUser = new User(UUID.randomUUID(), validUser.firstName(), validUser.lastName(), validUser.email(), validUser.image(), validUser.password(), UserRole.USER, LocalDateTime.now());
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        //Act
        Throwable exception = Assertions.catchException(() -> userService.save(validUser));

        //Assert
        assertThat(exception).isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("E-mail já cadastrado.");

        verify(userRepository, never()).save(repositorySavedUser);
    }

    @Test
    @DisplayName("should retrieve user information when receiving existing id")
    public void getUserByIdTest(){
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(
                userId,
                "John",
                "Doe",
                "doe@gmail.com",
                "",
                UserRole.USER
        );
        User repositorySavedUser = new User(userId, userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.image(), "adssasda", UserRole.USER, LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(repositorySavedUser));

        UserDTO retrievedUser = userService.getUserById(userId);

        assertThat(retrievedUser.id()).isEqualTo(userId);
    }

    @Test
    @DisplayName("should allow to update user information when receiving existing id")
    public void updateUserByIdTest(){
        // Arrange
        UUID userUUID = UUID.randomUUID();
        User retrievedUser = User.builder()
                .id(userUUID)
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.USER)
                .email("doe@gmail.com")
                .createdAt(LocalDateTime.now())
                .passwordHash("123")
                .image("")
                .build();

        CreateUserDTO userUpdateDTO = new CreateUserDTO(
                "John",
                "Doe Doe",
                "doedoe@gmail.com",
                "",
                "123"
        );

        User repositoryUpdatedUser = User.builder()
                .id(userUUID)
                .firstName(userUpdateDTO.firstName())
                .lastName(userUpdateDTO.lastName())
                .role(UserRole.USER)
                .email(userUpdateDTO.email())
                .createdAt(retrievedUser.getCreatedAt())
                .passwordHash("123")
                .image(userUpdateDTO.image())
                .build();

        when(userRepository.findById(userUUID)).thenReturn(Optional.of(retrievedUser));
        when(userRepository.save(any(User.class))).thenReturn(repositoryUpdatedUser);

        // Act
        UserDTO updatedUser = userService.updateUser(userUUID, userUpdateDTO);

        // Assert
        assertThat(updatedUser.id()).isEqualTo(userUUID);
        assertThat(updatedUser.firstName()).isEqualTo(userUpdateDTO.firstName());
        assertThat(updatedUser.lastName()).isEqualTo(userUpdateDTO.lastName());
        assertThat(updatedUser.email()).isEqualTo(userUpdateDTO.email());

        // Verify that save was called with the correct user
        verify(userRepository, times(1)).save(any(User.class));
        // Verify that findById was called
        verify(userRepository, times(1)).findById(userUUID);
    }
}