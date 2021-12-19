package com.manning.liveproject.simplysend.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
public class TokenService {

    private final JwtProperties jwtProperties;

    public String generateToken(Authentication authentication) {
        Algorithm algorithm = jwtProperties.getAlgorithmOrDefault();
        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(Date.from(Instant.now().plus(SecurityConstants.TOKEN_EXPIRY_TIME, ChronoUnit.MILLIS)))
                .sign(algorithm);
    }
}
