package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
import com.dsllt.oTravel_api.core.exceptions.EmailAlreadyExistsException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO save(CreateUserDTO createUserDTO) {
        if(userRepository.existsByEmail(createUserDTO.email())){
            throw new EmailAlreadyExistsException("E-mail já cadastrado.");
        }
        User user = User.createNewUserFromCreateUserDTO(createUserDTO);

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(), savedUser.getImage(), savedUser.getRole());
    }


    public UserDTO getUserById(UUID userUUID) {
        User user = userRepository.findById(userUUID).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
        return mapUserToUserDTO(user);
    }

    public UserDTO updateUser(UUID userUUID, CreateUserDTO updateUserDTO) {
        User retrievedUser = findUserById(userUUID);
        User updatedUser = updateUser(retrievedUser, updateUserDTO);
        User savedUser = userRepository.save(updatedUser);
        return mapUserToUserDTO(savedUser);
    }

    private User findUserById(UUID userUUID) {
        return userRepository.findById(userUUID)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
    }

    private UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getImage(),
                user.getRole()
        );
    }
    private User updateUser(User retrievedUser, CreateUserDTO updateUserDTO){
        if(updateUserDTO.firstName() != null && !retrievedUser.getFirstName().isEmpty()){
            retrievedUser.setFirstName(updateUserDTO.firstName());
        }
        if(updateUserDTO.lastName() != null && !retrievedUser.getLastName().isEmpty()){
            retrievedUser.setLastName(updateUserDTO.lastName());
        }
        if(updateUserDTO.email() != null && !retrievedUser.getEmail().isEmpty()){
            retrievedUser.setEmail(updateUserDTO.email());
        }
        if(updateUserDTO.image() != null && !retrievedUser.getImage().isEmpty()){
            retrievedUser.setImage(updateUserDTO.image());
        }

        return retrievedUser;
    }
}
