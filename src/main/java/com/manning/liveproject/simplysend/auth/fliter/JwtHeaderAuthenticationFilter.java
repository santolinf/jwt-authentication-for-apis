package com.manning.liveproject.simplysend.auth.fliter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import com.manning.liveproject.simplysend.auth.service.InMemorySessionService;
import com.manning.liveproject.simplysend.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_AUTHORITY_CLAIM_NAME;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class JwtHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final InMemorySessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String encodedToken  = request.getHeader("Authorization");

        if (tokenService.isNullOrEmptyOrNotTokenString(encodedToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT token = tokenService.verifyToken(encodedToken);
            String username = token.getSubject();
            List<SimpleGrantedAuthority> authorities =
                    ofNullable(token.getClaim(TOKEN_AUTHORITY_CLAIM_NAME))
                            .map(claim -> claim.asList(String.class).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                            .orElse(Lists.newArrayList());

            if (sessionService.exists(username)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT: {}", authentication);
            } else {
                log.debug("JWT: Invalid User Session");
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
            }
        } catch (TokenExpiredException e) {
            log.debug("JWT: {}", e.getMessage());
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer error=\"invalid_token\", error_description=\"Expired\"");
        }

        filterChain.doFilter(request, response);
    }
}
