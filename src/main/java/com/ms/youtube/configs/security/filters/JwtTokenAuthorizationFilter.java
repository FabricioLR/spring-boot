package com.ms.youtube.configs.security.filters;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ms.youtube.configs.security.JwtConfiguration;
import com.ms.youtube.configs.security.token.TokenConverter;
import com.ms.youtube.configs.security.utils.SecurityContextUtil;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {
    private final JwtConfiguration jwtConfiguration;
    private final TokenConverter tokenConverter;

    public JwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter){
        this.jwtConfiguration = jwtConfiguration;
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("verify token initialize");
            String header = request.getHeader(jwtConfiguration.getHeader().getName());
            if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())){
                throw new IOException("invalid token");
            }

            String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

            SecurityContextUtil.setSecurityContext(StringUtils.equalsAnyIgnoreCase("signed", jwtConfiguration.getType()) ? validate(token) : decryptValidating(token));
            log.info("verify token success");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            //log.info("verify token error " + e.toString());
            //filterChain.doFilter(request, response);
            response.getWriter().write(e.toString());
            response.getWriter().flush();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        String[] paths = new String[]{"/", "/login"};
        log.info(requestPath + ", " + Arrays.asList(paths).contains(requestPath));
        return Arrays.asList(paths).contains(requestPath);
    }

    @SneakyThrows
    private SignedJWT decryptValidating(String encryptedToken){
        String signedToken = tokenConverter.decryptToken(encryptedToken);
        tokenConverter.validateTokenSignature(signedToken);
        return SignedJWT.parse(signedToken);
    }

    @SneakyThrows
    private SignedJWT validate(String signedToken){
        tokenConverter.validateTokenSignature(signedToken);
        return SignedJWT.parse(signedToken);
    }
}