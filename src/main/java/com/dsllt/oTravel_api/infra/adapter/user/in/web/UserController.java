package com.dsllt.oTravel_api.infra.adapter.user.in.web;

import com.dsllt.oTravel_api.domain.user.model.User;
import com.dsllt.oTravel_api.domain.user.service.UserService;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.mapper.CreateUserResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserRequestIn;
import com.dsllt.oTravel_api.infra.adapter.user.in.web.model.CreateUserResponseOut;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CreateUserResponseOutMapper createUserResponseOutMapper;

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponseOut> register(@RequestBody @Valid CreateUserRequestIn createUserRequestIn, UriComponentsBuilder uriComponentsBuilder){
        var registerResponse = userService.create(createUserRequestIn);
        var uri = uriComponentsBuilder.path("/api/v1/users/register/{userUUID}").buildAndExpand(registerResponse.getId()).toUri();
        var createUserResponseOut = createUserResponseOutMapper.toCreateUserResponseOut(registerResponse);
        return ResponseEntity.created(uri).body(createUserResponseOut);
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<CreateUserResponseOut> getUserById(@Nonnull @PathVariable UUID userUUID) {
        User user = userService.findUser(userUUID);
        var createUserResponseOut = createUserResponseOutMapper.toCreateUserResponseOut(user);
        return ResponseEntity.ok().body(createUserResponseOut);
    }

    @PutMapping("/{userUUID}")
    public ResponseEntity<CreateUserResponseOut> updateUser(@Nonnull @PathVariable UUID userUUID,
                                                            @RequestBody CreateUserRequestIn updateUserDTO) {
        User user = userService.updateUser(userUUID, updateUserDTO);
        var createUserResponseOut = createUserResponseOutMapper.toCreateUserResponseOut(user);
        return ResponseEntity.ok().body(createUserResponseOut);
    }

}
