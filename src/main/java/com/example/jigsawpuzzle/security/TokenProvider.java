package com.example.jigsawpuzzle.security;

import io.jsonwebtoken.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private final String jwtSecret = "3Zkp9YA/bY4aB+rITjpMqtRqTThCsK1rEWjwdUUGVbM="; // Mã bí mật dùng để mã hóa và giải mã JWT
    private final long accessTokenExpiration = 86400000L; // 1 ngày
    private final long refreshTokenExpiration = 604800000L; // 7 ngày
    public String createAccessToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        Long principal = (Long) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(String.valueOf(principal))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    public String getSubject(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roles", List.class).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public Date getIssuedAtDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getIssuedAt();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public boolean validateToken(String token, com.example.jigsawpuzzle.domain.User userDetails) {
        String userId = getSubject(token);
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Map<String, Object> getAllClaimsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims;
    }
}