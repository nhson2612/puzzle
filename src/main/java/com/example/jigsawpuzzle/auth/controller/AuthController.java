package com.example.jigsawpuzzle.auth.controller;

import com.example.jigsawpuzzle.auth.service.AuthService;
import com.example.jigsawpuzzle.auth.dto.AuthResponse;
import com.example.jigsawpuzzle.auth.dto.LoginReq;
import com.example.jigsawpuzzle.auth.dto.RegisterReq;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "This endpoint allows users to register with a username, email, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterReq registerReq) {
        AuthResponse response = authService.register(registerReq.username(), registerReq.password(), registerReq.email());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login a user", description = "This endpoint allows users to login with their username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid login credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginReq loginReq) {
        AuthResponse response = authService.login(loginReq.username(), loginReq.password());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout user", description = "This endpoint allows a user to logout by providing a valid Bearer token in the Authorization header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Missing or invalid token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token")
    })
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

    @Operation(summary = "Refresh access token", description = "This endpoint allows a user to refresh their access token by providing a valid refresh token in the Authorization header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Missing or invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired refresh token")
    })
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
