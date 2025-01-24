package com.dsllt.oTravel_api.infra.controller;

import com.dsllt.oTravel_api.infra.dto.LoginResponseDTO;
import com.dsllt.oTravel_api.infra.dto.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import com.dsllt.oTravel_api.core.usecase.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        var authResponse = authenticationService.login(authenticationDTO);

        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CreateUserDTO userRegisterDTO, UriComponentsBuilder uriComponentsBuilder){
        var registerResponse = authenticationService.register(userRegisterDTO);

        var uri = uriComponentsBuilder.path("/api/v1/auth/{userUUID}").buildAndExpand(registerResponse.id()).toUri();

        return ResponseEntity.ok().body(registerResponse);
    }
}
