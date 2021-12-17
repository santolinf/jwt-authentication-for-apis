package com.manning.liveproject.simplysend.auth.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.ImmutableMap;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.function.Function;

public class SimplySendAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Map<String, Function<String, Algorithm>> SUPPORTED_ALGORITHMS = ImmutableMap.of(
            "HS256", Algorithm::HMAC256,
            "HS384", Algorithm::HMAC384,
            "HS512", Algorithm::HMAC512
    );

    private final JwtProperties jwtProperties;

    public SimplySendAuthenticationSuccessHandler(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Function<String, Algorithm> algorithm = SUPPORTED_ALGORITHMS.getOrDefault(jwtProperties.getAlgorithm(), Algorithm::HMAC512);
        String token = JWT.create()
                .withSubject(authentication.getName())
                .withIssuer(jwtProperties.getIssuer())
                .sign(algorithm.apply(jwtProperties.getSecret()));

        response.setHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token);
        response.setStatus(HttpStatus.OK.value());
    }
}
