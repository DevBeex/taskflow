package com.trello.trello.controller.auth;

import com.trello.trello.constants.ErrorMessages;
import com.trello.trello.constants.JwtMessages;
import com.trello.trello.dto.auth.JwtResponse;
import com.trello.trello.dto.auth.LoginRequest;
import com.trello.trello.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());
            JwtResponse jwtResponse = new JwtResponse(jwt);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new JwtResponse(ErrorMessages.USER_NOT_FOUND));
        }
    }
}