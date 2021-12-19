package com.manning.liveproject.simplysend.auth.fliter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.collect.Lists;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.service.InMemorySessionService;
import com.manning.liveproject.simplysend.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final InMemorySessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token  = request.getHeader("Authorization");

        if (tokenService.isNullOrEmptyOrNotTokenString(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = tokenService.verifyToken(token).getSubject();

            if (sessionService.exists(username)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Lists.newArrayList()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT: {}", authentication);
            } else {
                log.debug("JWT: Invalid User Session");
                response.setHeader(SecurityConstants.HEADER_X_AUTH_MESSAGE, "Invalid User Session");
            }
        } catch (TokenExpiredException e) {
            log.debug("JWT: {}", e.getMessage());
            response.setHeader(SecurityConstants.HEADER_X_AUTH_MESSAGE, "Token expired");
        }

        filterChain.doFilter(request, response);
    }
}
