package com.manning.liveproject.simplysend.auth.config;

import lombok.Data;

@Data
public class JwtProperties {

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
}
