package com.trello.trello.controller;

import com.trello.trello.dto.auth.JwtResponse;
import com.trello.trello.dto.auth.LoginRequest;
import com.trello.trello.dto.response.ApiResponse;
import com.trello.trello.model.User;
import com.trello.trello.repository.UserRepository;
import com.trello.trello.service.ActiveSessionService;
import com.trello.trello.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final ActiveSessionService activeSessionService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtils,
                          ActiveSessionService activeSessionService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.activeSessionService = activeSessionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request) {
        try {
            // 1. Autenticación
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Obtener usuario
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 3. Generar token JWT
            String jwt = jwtUtils.generateJwtToken(user);

            // 4. Obtener dirección IP
            String ipAddress = getClientIP(request);

            // 5. Información del dispositivo
            String deviceInfo = userAgent + " - " + ipAddress;

            // 6. Crear sesión activa
            activeSessionService.createSession(deviceInfo, jwt, user);

            // 7. Respuesta al usuario
            JwtResponse jwtResponse = new JwtResponse(jwt);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new JwtResponse("Usuario no encontrado o credenciales inválidas"));
        }
    }

    // Metodo auxiliar para obtener la dirección IP del cliente
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(@RequestHeader(name = "Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            activeSessionService.deleteSession(token);
            ApiResponse response = new ApiResponse(200, "Success", "Sesión cerrada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponse response = new ApiResponse(400, ex.getCause().toString(), ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}