package com.manning.liveproject.simplysend.auth.fliter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
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

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token  = request.getHeader("Authorization");

        if (Strings.isNullOrEmpty(token) || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = JWT.require(jwtProperties.getAlgorithmOrDefault())
                    .build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Lists.newArrayList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("JWT: {}", authentication);
        } catch (TokenExpiredException e) {
            log.debug("JWT: {}", e.getMessage());
            response.setHeader(SecurityConstants.HEADER_X_AUTH_MESSAGE, "Token expired");
        }

        filterChain.doFilter(request, response);
    }
}
