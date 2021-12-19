package com.manning.liveproject.simplysend.auth;

public final class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long TOKEN_EXPIRY_TIME = 86_400_000;    // 24 hours
    public static final String SIGN_UP_URL = "/users";
    public static final String LOGIN_URL = "/login";
    public static final String LOGIN_KEY_USERNAME = "emailId";

    public static final String HEADER_X_AUTH_MESSAGE = "X-Auth-Message";

    private SecurityConstants() {}
}
