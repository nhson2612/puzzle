package com.example.jigsawpuzzle.auth.service;

import com.example.jigsawpuzzle.auth.repository.UserRepository;
import com.example.jigsawpuzzle.domain.Email;
import com.example.jigsawpuzzle.domain.User;
import com.example.jigsawpuzzle.auth.dto.AuthResponse;
import com.example.jigsawpuzzle.security.BlacklistTokenManager;
import com.example.jigsawpuzzle.security.JwtAuthentication;
import com.example.jigsawpuzzle.security.TokenProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BlacklistTokenManager blacklistTokenManager;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, BlacklistTokenManager blacklistTokenManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.blacklistTokenManager = blacklistTokenManager;
    }

    public AuthResponse register(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        Email emailObj = new Email(email);
        if (userRepository.existsByEmail(emailObj.getAddress())) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        User user = User.builder()
                .username(username)
                .password(password)
                .email(emailObj)
                .avatarUrl(null)
                .createdAt(LocalDateTime.now())
                .isOnline(false)
                .build();
        User saved = userRepository.save(user);
        JwtAuthentication authenticated = JwtAuthentication.authenticated(saved.getId(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String accessToken = tokenProvider.createAccessToken(authenticated);
        String refreshToken = tokenProvider.createRefreshToken(authenticated);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("Invalid username or password.");
//        }
        JwtAuthentication authenticated = JwtAuthentication.authenticated(user.getId(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String accessToken = tokenProvider.createAccessToken(authenticated);
        String refreshToken = tokenProvider.createRefreshToken(authenticated);
        return new AuthResponse(accessToken, refreshToken);
    }

    public void logout(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        blacklistTokenManager.revokeToken(token);
    }
    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token.");
        }
        String userId = tokenProvider.getSubject(refreshToken);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        JwtAuthentication authenticated = JwtAuthentication.authenticated(user.getId(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String accessToken = tokenProvider.createAccessToken(authenticated);
        return new AuthResponse(accessToken, refreshToken);
    }
}
