package com.dsllt.oTravel_api.core.entity.user;

import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String image;
    private String password;
    private UserRole role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public static User createNewUserFromCreateUserDTO(CreateUserDTO createUserDTO){
        User user = new User();
        user.setFirstName(createUserDTO.firstName());
        user.setLastName(createUserDTO.lastName());
        user.setEmail(createUserDTO.email());
        user.setPassword(createUserDTO.password());
        user.setImage(createUserDTO.image());
        user.setRole(UserRole.USER);
        return user;
    }
}
