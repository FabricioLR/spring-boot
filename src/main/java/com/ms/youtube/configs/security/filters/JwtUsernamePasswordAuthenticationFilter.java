package com.ms.youtube.configs.security.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.youtube.configs.security.JwtConfiguration;
import com.ms.youtube.configs.security.token.TokenCreator;
import com.ms.youtube.models.UserModel;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    final AuthenticationManager authenticationManager;
    final JwtConfiguration jwtConfiguration;
    final TokenCreator tokenCreator;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, TokenCreator tokenCreator){
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.tokenCreator = tokenCreator;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException{
        log.info("authentication initialize");
        UserModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
        if (userModel == null){
            throw new UsernameNotFoundException("invalid credentials");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword(), Collections.emptyList());

        usernamePasswordAuthenticationToken.setDetails(userModel);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserModel userModel = (UserModel)authResult.getPrincipal();
        SignedJWT signedJWT = tokenCreator.createSignedJWT(userModel);

        String encryptedToken = tokenCreator.encryptToken(signedJWT);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        log.info("response " + new Gson().toJson(authResult).toString());
        response.getWriter().print(new Gson().toJson(authResult));
        response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader().getName());
        response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptedToken);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("invalid credentials");
        response.getWriter().flush();
    }
}