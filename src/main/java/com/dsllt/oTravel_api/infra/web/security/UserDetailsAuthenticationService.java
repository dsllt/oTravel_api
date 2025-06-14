package com.dsllt.oTravel_api.infra.web.security;

import com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsAuthenticationService implements UserDetailsService {
    UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findByEmail(username);
    }

}
