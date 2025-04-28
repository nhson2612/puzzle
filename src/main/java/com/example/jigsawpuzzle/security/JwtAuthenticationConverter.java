package com.example.jigsawpuzzle.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private final TokenProvider tokenProvider;
    private final BlacklistTokenManager blacklistTokenManager;

    public JwtAuthenticationConverter(TokenProvider tokenProvider, BlacklistTokenManager blacklistTokenManager) {
        this.tokenProvider = tokenProvider;
        this.blacklistTokenManager = blacklistTokenManager;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header == null){
            return null;
        }else{
            if(!StringUtils.startsWithIgnoreCase(header,"Bearer")){
                return null;
            } else if (header.equalsIgnoreCase("Bearer")) {
                throw new BadCredentialsException("Empty JWT");
            }else{
                String token = header.substring(6);
                if (blacklistTokenManager.isRevoked(token)) {  // Kiểm tra nếu token bị revoke
                    throw new BadCredentialsException("Authentication failed: Token is revoked.");
                }

                String subject = tokenProvider.getSubject(token);
                Long userId;
                try {
                    userId = Long.parseLong(subject);
                } catch (NumberFormatException e) {
                    throw new BadCredentialsException("Invalid JWT subject, expected Long value");
                }
                List<GrantedAuthority> authorities = tokenProvider.getRolesFromToken(token);
                if(userId == null){
                    throw new BadCredentialsException("Invalid jwt");
                }else{
                    JwtAuthentication jwtAuthentication = new JwtAuthentication(userId,"",authorities);
                    return jwtAuthentication;
                }
            }
        }
    }
}
