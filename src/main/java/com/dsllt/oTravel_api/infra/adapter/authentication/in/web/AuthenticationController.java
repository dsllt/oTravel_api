package com.dsllt.oTravel_api.infra.adapter.authentication.in.web;

import com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model.LoginResponseOut;
import com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model.AuthenticationRequestIn;
import com.dsllt.oTravel_api.domain.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<LoginResponseOut> login(@RequestBody @Valid AuthenticationRequestIn authenticationRequestIn){

        var authResponse = authenticationService.login(authenticationRequestIn);
        return ResponseEntity.ok().body(authResponse);
    }
}
