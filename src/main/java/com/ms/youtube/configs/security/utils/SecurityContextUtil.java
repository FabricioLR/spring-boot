package com.ms.youtube.configs.security.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ms.youtube.models.RoleModel;
import com.ms.youtube.models.UserModel;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityContextUtil {
    public SecurityContextUtil(){}

    public static void setSecurityContext(SignedJWT signedJWT) throws JOSEException{
        log.info("set security context");
        try {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String username = claims.getSubject();
            Date date = claims.getExpirationTime();

            if (username == null){
                throw new JOSEException("username missing from JWT");
            }

            if (new Date().compareTo(date) > 0){
                throw new JOSEException("token expired");
            }

            log.info("valid token");

            List<String> authorities = claims.getStringListClaim("authorities");
            List<RoleModel> roleModelAuthorities = new ArrayList<RoleModel>();
            authorities.stream()
                .forEach(authority -> {
                    RoleModel roleModel = new RoleModel();
                    roleModel.setRoleName(authority);
                    roleModelAuthorities.add(roleModel);
                });

            UserModel userModel = new UserModel();
            userModel.setUsername(username);
            userModel.setId(UUID.fromString(claims.getStringClaim("userId")));
            userModel.setRoles(roleModelAuthorities);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userModel, null, createAuthorities(authorities));
            auth.setDetails(signedJWT.serialize());

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception error) {
            log.info("set security context error " + error.getMessage());
            SecurityContextHolder.clearContext();
            throw new JOSEException(error.getMessage());
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities){
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
