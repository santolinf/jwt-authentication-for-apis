package com.manning.liveproject.simplysend.auth.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.Map;
import java.util.function.Function;

@Data
public class JwtProperties {

    private static final Map<String, Function<String, Algorithm>> SUPPORTED_ALGORITHMS = ImmutableMap.of(
            "HS256", Algorithm::HMAC256,
            "HS384", Algorithm::HMAC384,
            "HS512", Algorithm::HMAC512
    );

    private static final String DEFAULT_ISSUER_NAME = "SimplySend";
    private static final String DEFAULT_ALGORITHM_NAME = "HS512";

    /**
     * Claim that specifies the issuer of the token
     */
    private String issuer = DEFAULT_ISSUER_NAME;

    /**
     * Any standard JWT signature algorithm.
     * For example, the following HMAC algorithms can be used: HS256, HS384 and HS512.
     */
    private String algorithm = DEFAULT_ALGORITHM_NAME;

    /**
     * This is the symmetrical hash key secret. Deliberately not set, so that applications are forced to set it!
     */
    private String secret;

    /**
     * Return the configured Algorithm object initialised with the secret for signing.
     * Default to a predefined Algorithm otherwise.
     */
    public Algorithm getAlgorithmOrDefault() {
        return SUPPORTED_ALGORITHMS.getOrDefault(algorithm, Algorithm::HMAC512).apply(secret);
    }
}
