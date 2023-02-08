package com.ms.youtube.configs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ms.youtube.configs.security.filters.JwtTokenAuthorizationFilter;
import com.ms.youtube.configs.security.filters.JwtUsernamePasswordAuthenticationFilter;
import com.ms.youtube.configs.security.token.TokenConverter;
import com.ms.youtube.configs.security.token.TokenCreator;

@Configuration
public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity>{
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http
            .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager, new JwtConfiguration(), new TokenCreator(new JwtConfiguration())))
            .addFilterBefore(new JwtTokenAuthorizationFilter(new JwtConfiguration(), new TokenConverter(new JwtConfiguration())), UsernamePasswordAuthenticationFilter.class);
        }
}
