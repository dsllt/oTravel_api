package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.dtos.user.CreateUserDTO;
import com.dsllt.oTravel_api.dtos.user.UserDTO;
import com.dsllt.oTravel_api.service.user.UserService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid CreateUserDTO createUserDTO, UriComponentsBuilder uriComponentsBuilder){
        UserDTO newUser = userService.save(createUserDTO);

        var uri = uriComponentsBuilder.path("/api/v1/user/{userUUID}").buildAndExpand(newUser.id()).toUri();

        return ResponseEntity.created(uri).body(newUser);
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
