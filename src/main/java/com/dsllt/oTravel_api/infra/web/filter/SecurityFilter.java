package com.dsllt.oTravel_api.infra.web.filter;

import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import com.dsllt.oTravel_api.infra.web.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);

        try{
            if(token != null) {
                var userEmail = tokenService.validateToken(token);
                UserDetails user = userJpaRepository.findByEmail(userEmail);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
        }

    }

    private String recoverToken(HttpServletRequest request){
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null) return null;

        return authorizationHeader.replace("Bearer ", "");
    }
}
