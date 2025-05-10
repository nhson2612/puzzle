package com.example.jigsawpuzzle.config;

import com.example.jigsawpuzzle.auth.repository.UserRepository;
import com.example.jigsawpuzzle.security.JwtAuthenticationConverter;
import com.example.jigsawpuzzle.security.JwtAuthenticationFilter;
import com.example.jigsawpuzzle.security.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userRepository);
        return new ProviderManager(daoAuthenticationProvider,jwtAuthenticationProvider);
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtAuthenticationConverter jwtAuthenticationConverter) {
        return new JwtAuthenticationFilter(jwtAuthenticationConverter, authenticationManager);
    }
}