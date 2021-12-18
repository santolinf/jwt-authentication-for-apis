package com.manning.liveproject.simplysend.auth.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
public class SimplySendAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Algorithm algorithm = jwtProperties.getAlgorithmOrDefault();
        String token = JWT.create()
                .withSubject(authentication.getName())
                .withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(Date.from(Instant.now().plus(SecurityConstants.TOKEN_EXPIRY_TIME, ChronoUnit.MILLIS)))
                .sign(algorithm);

        response.setHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token);
        response.setStatus(HttpStatus.OK.value());
    }
}
