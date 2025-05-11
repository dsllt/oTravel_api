package com.dsllt.oTravel_api.domain.authentication.service;

import com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model.AuthenticationRequestIn;
import com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model.LoginResponseOut;
import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserEntity;
import com.dsllt.oTravel_api.infra.web.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public LoginResponseOut login(AuthenticationRequestIn authenticationRequestIn) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequestIn.email(),
                authenticationRequestIn.password());
        var auth = authenticationManager.authenticate(authenticationToken);
        var user = (UserEntity) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        return LoginResponseOut.builder()
                .userId(user.getId())
                .token(token)
                .build();
    }
}
