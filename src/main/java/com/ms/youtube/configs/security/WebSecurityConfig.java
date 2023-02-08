package com.ms.youtube.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {
    @Autowired
    JwtConfiguration jwtConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers(jwtConfiguration.getLoginUrl()).permitAll()
                .requestMatchers(jwtConfiguration.getRegisterUrl()).permitAll()
                .requestMatchers(HttpMethod.POST, "/saveUser").permitAll()
                .requestMatchers(HttpMethod.PUT, "/updateUserRole").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/getEmails").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .anyRequest().authenticated()
            .and()
            .apply(new CustomDsl())
            .and()
            .csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
