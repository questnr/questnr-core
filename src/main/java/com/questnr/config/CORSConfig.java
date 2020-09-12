package com.questnr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSConfig {
    @Value("${app.is-prod}")
    boolean isProd;

    @Bean
    public CorsFilter corsFilter() {
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("https://questnr.com");
        allowedOrigins.add("https://www.questnr.com");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // @Todo Don't do this in production, use a proper list  of allowed origins
        config.setAllowedOrigins(isProd ? allowedOrigins : Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
