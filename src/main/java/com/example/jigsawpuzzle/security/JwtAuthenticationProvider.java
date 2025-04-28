package com.example.jigsawpuzzle.security;

import com.example.jigsawpuzzle.domain.User;
import com.example.jigsawpuzzle.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    public JwtAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long userIdReq = null;
        try {
            userIdReq = Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Invalid JWT subject format, expected Long value.");
        }
        Long finalUserIdReq = userIdReq;
        User user = userRepository.findById(userIdReq).orElseThrow(() -> new BadCredentialsException("User not found with id: " + finalUserIdReq));

        if (user.getId().equals(userIdReq)) {
            return JwtAuthentication.authenticated(
                    userIdReq,
                    "",
                    user.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("User ID in JWT does not match with the provided user.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}
