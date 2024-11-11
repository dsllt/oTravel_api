package com.dsllt.oTravel_api.service;

import com.dsllt.oTravel_api.dtos.LoginResponseDTO;
import com.dsllt.oTravel_api.dtos.authentication.AuthenticationDTO;
import com.dsllt.oTravel_api.entity.user.User;
import com.dsllt.oTravel_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsAuthenticationService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

}
