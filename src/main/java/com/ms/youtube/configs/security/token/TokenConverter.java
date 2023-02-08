package com.ms.youtube.configs.security.token;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Service;

import com.ms.youtube.configs.security.JwtConfiguration;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import lombok.SneakyThrows;

@Service
public class TokenConverter {
    private final JwtConfiguration jwtConfiguration;

    public TokenConverter(JwtConfiguration jwtConfiguration){
        this.jwtConfiguration = jwtConfiguration;
    }

    @SneakyThrows
    public String decryptToken(String encryptedToken){
        JWEObject jweObject = JWEObject.parse(encryptedToken);

        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());

        jweObject.decrypt(directDecrypter);

        return jweObject.getPayload().toSignedJWT().serialize();
    }

    @SneakyThrows
    public void validateTokenSignature(String signedToken){
        SignedJWT signedJWT = SignedJWT.parse(signedToken);

        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());

        if (!signedJWT.verify(new RSASSAVerifier(publicKey))){
            throw new AccessDeniedException("invalid token signature");
        }
        
    }
}
