package com.ms.youtube.configs.security.token;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.ms.youtube.configs.security.JwtConfiguration;
import com.ms.youtube.models.UserModel;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenCreator {
    final JwtConfiguration jwtConfiguration;

    public TokenCreator(JwtConfiguration jwtConfiguration){
        this.jwtConfiguration = jwtConfiguration;
    }

    @SneakyThrows
    public SignedJWT createSignedJWT(UserModel userModel){
        log.info("create signed token");
        JWTClaimsSet jwtClaimsSet = createJWTClaimsSet(userModel);

        log.info("generating key pair");
        KeyPair rsaKeys = generateKeyPair();

        JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk).type(JOSEObjectType.JWT).build(), jwtClaimsSet);

        RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

        signedJWT.sign(signer);

        //log.info("token " + signedJWT.serialize());

        return signedJWT;
    }

    private JWTClaimsSet createJWTClaimsSet(UserModel userModel){
        log.info("generation claims set");
        return new JWTClaimsSet.Builder()
            .subject(userModel.getUsername())
            .claim("authorities", userModel.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .claim("userId", userModel.getId())
            .issuer("http://fabricio.com")
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
            .build();
    }
    @SneakyThrows
    private KeyPair generateKeyPair(){
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        return keyPairGenerator.genKeyPair();
    }

    public String encryptToken(SignedJWT token) throws JOSEException {
        log.info("encrypting signed token");
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());

        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(), new Payload(token));
    
        jweObject.encrypt(directEncrypter);

        return jweObject.serialize();
    }
}
