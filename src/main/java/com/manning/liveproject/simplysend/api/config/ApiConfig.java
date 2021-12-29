package com.manning.liveproject.simplysend.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class ApiConfig {

    @Value("${simplysend.web.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            return config;
        };
    }
}
