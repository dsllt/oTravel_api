package com.dsllt.oTravel_api.core.usecase.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.core.exceptions.TokenGenerationException;
import com.dsllt.oTravel_api.core.exceptions.TokenVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("oTravel-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new TokenGenerationException("Erro ao gerar token.");
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("oTravel-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new TokenVerificationException("Erro ao verificar token. Token JWT inválido ou expirado.");
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(48).toInstant(ZoneOffset.of("-03:00"));
    }
}
