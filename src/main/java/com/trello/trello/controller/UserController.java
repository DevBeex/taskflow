package com.trello.trello.controller;

import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto user) {
        UserResponseDto savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

}
