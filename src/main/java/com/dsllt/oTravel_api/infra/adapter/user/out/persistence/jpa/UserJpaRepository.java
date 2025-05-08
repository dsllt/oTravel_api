package com.dsllt.oTravel_api.infra.adapter.authentication.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
}
