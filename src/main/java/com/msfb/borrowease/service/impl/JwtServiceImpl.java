package com.msfb.borrowease.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.msfb.borrowease.entity.JwtClaims;
import com.msfb.borrowease.entity.User;
import com.msfb.borrowease.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final String JWT_SECRET;
    private final String JWT_ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${borrow_ease.jwt.secret_key}") String jwtSecret,
            @Value("${borrow_ease.jwt.issuer}") String jwtIssuer,
            @Value("${borrow_ease.jwt.expiration-in-second}") long jwtExpiration) {
        JWT_SECRET = jwtSecret;
        JWT_ISSUER = jwtIssuer;
        JWT_EXPIRATION = jwtExpiration;
    }

    @Override
    public String generateJwtToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(user.getId())
                    .withClaim("role", user.getRole().name())
                    .withIssuer(JWT_ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while creating jwt token");
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parseJwtToken(token));
            return JwtClaims.builder()
                    .userId(decodedJWT.getSubject())
                    .role(decodedJWT.getClaim("role").toString())
                    .build();
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return null;
        }
    }

    private String parseJwtToken(String token) {
        if (token == null) return null;
        return token.replace("Bearer ", "");
    }
}
