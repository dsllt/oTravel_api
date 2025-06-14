package com.dsllt.oTravel_api.infra.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfiguration {

    @Value("${cors.origins}")
    private String corsOrigins;

    @Bean
    @NonNull
    CorsConfigurationSource getCorsConfigurationSource() {
        String[] origins = corsOrigins.split(",");

        org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(origins));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}