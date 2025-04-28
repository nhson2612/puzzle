package com.example.jigsawpuzzle.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.Collection;

public class JwtAuthentication implements Authentication {
    private Object principal;
    private Object credentials;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated = false;
    public JwtAuthentication(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = AuthorityUtils.NO_AUTHORITIES;
    }
    public JwtAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities == null ? AuthorityUtils.NO_AUTHORITIES : authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public Object getCredentials() {
        return credentials;
    }
    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }
    @Override
    public String getName() {
        return principal instanceof String ? (String) principal : principal.toString();
    }
    public static JwtAuthentication unauthenticated(Object principal, Object credentials) {
        return new JwtAuthentication(principal, credentials);
    }
    public static JwtAuthentication authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        JwtAuthentication authenticated = new JwtAuthentication(principal, credentials, authorities);
        authenticated.setAuthenticated(true);
        return authenticated;
    }
}

