package com.dsllt.oTravel_api.service;

import com.dsllt.oTravel_api.dtos.LoginResponseDTO;
import com.dsllt.oTravel_api.dtos.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;
import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    public UserDTO register(CreateUserDTO createUserDTO){
        String encyptedPassword = new BCryptPasswordEncoder().encode(createUserDTO.password());

        CreateUserDTO userCreateDTO = new CreateUserDTO(createUserDTO.firstName(), createUserDTO.lastName(), createUserDTO.email(), createUserDTO.image(), encyptedPassword);

        return userService.save(userCreateDTO);
    }

    public LoginResponseDTO login(AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        User user = (User) auth.getPrincipal();

        return new LoginResponseDTO("", user.getId());
    }
}
