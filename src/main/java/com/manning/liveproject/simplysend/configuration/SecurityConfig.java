package com.manning.liveproject.simplysend.configuration;

import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.auth.config.JwtProperties;
import com.manning.liveproject.simplysend.auth.fliter.JwtHeaderAuthenticationFilter;
import com.manning.liveproject.simplysend.auth.handler.SimplySendAuthenticationSuccessHandler;
import com.manning.liveproject.simplysend.auth.handler.SimplySendLogoutHandler;
import com.manning.liveproject.simplysend.auth.service.InMemorySessionService;
import com.manning.liveproject.simplysend.auth.service.SimplySendUserDetailsService;
import com.manning.liveproject.simplysend.auth.service.TokenService;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAccountRepository userAccountRepository;
    private final InMemorySessionService sessionService;

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

    @Bean
    public TokenService tokenService() {
        return new TokenService(jwtProperties());
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(
                // allow access to swagger
                "/**/swagger**/**",
                "/**/api-docs**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final HttpStatusEntryPoint failedAuthenticationEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

        http.headers().defaultsDisabled()
                // set to DENY in order to prevent the API responses being loaded in a frame or iframe
                .frameOptions().deny()
                // the current guidance is to set to “0” on API responses to completely disable these protections due to security issues they can also introduce
                .xssProtection().xssProtectionEnabled(false).and()
                // policy for restricting where content can be loaded from
                // default-src none prevents the response from loading any scripts or resources
                // frame-ancestors none replacement for X-Frame-Options, this prevents the response being loaded into an iframe
                // sandbox n/a disables scripts and other potentially dangerous content from being executed
                .contentSecurityPolicy("default-src 'none'; frame-ancestors 'none'; sandbox").and()
                // set to nosniff to prevent the browser guessing the correct Content-Type
                .contentTypeOptions().and()
                // always access via HTTPS
                .httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000).and()
                .cacheControl();

        http
                .addFilterBefore(
                        new JwtHeaderAuthenticationFilter(tokenService(), sessionService),
                        UsernamePasswordAuthenticationFilter.class
                )

                .authorizeHttpRequests()
                    .mvcMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                    .anyRequest().authenticated()

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
                    .successHandler(new SimplySendAuthenticationSuccessHandler(tokenService(), sessionService))

                .and().logout()
                    .addLogoutHandler(new SimplySendLogoutHandler(tokenService(), sessionService))
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))    // do not redirect

                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(failedAuthenticationEntryPoint)
        ;
    }
}
