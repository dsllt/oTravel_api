package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.dtos.LoginResponseDTO;
import com.dsllt.oTravel_api.dtos.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;
import com.dsllt.oTravel_api.service.AuthenticationService;
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

    @Autowired
    private AuthenticationService authenticationService;

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
