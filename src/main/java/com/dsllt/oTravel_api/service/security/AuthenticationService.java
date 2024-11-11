package com.dsllt.oTravel_api.service.security;

import com.dsllt.oTravel_api.dtos.LoginResponseDTO;
import com.dsllt.oTravel_api.dtos.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;
import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.service.exceptions.TokenVerificationException;
import com.dsllt.oTravel_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    public UserDTO register(CreateUserDTO createUserDTO){
        String encryptedPassword = new BCryptPasswordEncoder().encode(createUserDTO.password());

        CreateUserDTO userCreateDTO = new CreateUserDTO(createUserDTO.firstName(), createUserDTO.lastName(), createUserDTO.email(), createUserDTO.image(), encryptedPassword);

        return userService.save(userCreateDTO);
    }

    public LoginResponseDTO login(AuthenticationDTO authenticationDTO) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
            var auth = authenticationManager.authenticate(authenticationToken);

            System.out.println(auth.getPrincipal());

            User user = (User) auth.getPrincipal();
            String token = tokenService.generateToken(user);

            return new LoginResponseDTO(token, user.getId());
        } catch (AuthenticationException e){
            throw new TokenVerificationException("Erro ao verificar usuário, e-mail ou senha inválidos.");
        }

    }
}
