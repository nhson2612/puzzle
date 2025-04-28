package com.example.jigsawpuzzle.controllers;

import com.example.jigsawpuzzle.dto.AuthResponse;
import com.example.jigsawpuzzle.dto.LoginReq;
import com.example.jigsawpuzzle.dto.RegisterReq;
import com.example.jigsawpuzzle.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterReq registerReq) {
        AuthResponse response = authService.register(registerReq.username(), registerReq.password(), registerReq.email());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginReq loginReq){
        AuthResponse response = authService.login(loginReq.username(),loginReq.password());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing or invalid.");
        }
        String token = authHeader.substring(7);
        try {
            authService.logout(token);
            return ResponseEntity.ok("Logout successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing or invalid.");
        }
        String refreshToken = authHeader.substring(7);
        try {
            AuthResponse newTokens = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newTokens);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
        }
    }
}
