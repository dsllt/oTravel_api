package com.dsllt.oTravel_api.infra.controller;

import com.dsllt.oTravel_api.infra.dto.user.CreateUserDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<UserDTO> getUserById(@Nonnull @PathVariable UUID userUUID){
        UserDTO user = userService.getUserById(userUUID);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{userUUID}")
    public ResponseEntity<UserDTO> updateUser(@Nonnull @PathVariable UUID userUUID, @RequestBody CreateUserDTO updateUserDTO){
        UserDTO user = userService.updateUser(userUUID, updateUserDTO);

        return  ResponseEntity.ok().body(user);
    }

}
