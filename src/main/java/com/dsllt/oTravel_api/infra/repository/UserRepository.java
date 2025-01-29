package com.dsllt.oTravel_api.infra.repository;

import com.dsllt.oTravel_api.core.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    UserDetails findByEmail(String email);
}
