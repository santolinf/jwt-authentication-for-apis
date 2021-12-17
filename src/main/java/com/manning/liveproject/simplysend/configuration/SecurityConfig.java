package com.manning.liveproject.simplysend.configuration;

import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
import com.manning.liveproject.simplysend.auth.handler.SimplySendAuthenticationSuccessHandler;
import com.manning.liveproject.simplysend.auth.service.SimplySendUserDetailsService;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAccountRepository userAccountRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new SimplySendUserDetailsService(userAccountRepository);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConfigurationProperties(prefix = "simplysend.security.jwt-properties")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final HttpStatusEntryPoint failedAuthenticationEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
        http
                .authorizeHttpRequests()
                    .anyRequest().permitAll()

                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin()
                    .loginProcessingUrl(SecurityConstants.LOGIN_URL)
                    .usernameParameter(SecurityConstants.LOGIN_KEY_USERNAME)
                    .failureHandler(new AuthenticationEntryPointFailureHandler(failedAuthenticationEntryPoint))
                    .successHandler(new SimplySendAuthenticationSuccessHandler(jwtProperties()))

                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(failedAuthenticationEntryPoint)
        ;
    }
}
