package com.trello.trello.controller;

import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.service.UserService;
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
        UserResponseDto user = userService.findUserById(id);
        return user;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        UserResponseDto savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }
}
