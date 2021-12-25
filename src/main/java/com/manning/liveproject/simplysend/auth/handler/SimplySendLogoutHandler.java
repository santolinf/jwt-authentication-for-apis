package com.manning.liveproject.simplysend.auth.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.service.InMemorySessionService;
import com.manning.liveproject.simplysend.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User logout to ensure the user’s session is not misused after user operations are completed.
 */
@Slf4j
@RequiredArgsConstructor
public class SimplySendLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final InMemorySessionService sessionService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token  = request.getHeader("Authorization");

        if (tokenService.isNullOrEmptyOrNotTokenString(token)) {
            return;
        }

        try {
            String username = tokenService.verifyToken(token).getSubject();
            sessionService.remove(username);
        } catch (TokenExpiredException e) {
            log.debug("JWT: {}", e.getMessage());
            response.setHeader(SecurityConstants.HEADER_X_AUTH_MESSAGE, "Token expired");
        }
    }
}
