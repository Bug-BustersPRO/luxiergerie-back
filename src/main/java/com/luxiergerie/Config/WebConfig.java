package com.luxiergerie.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for web hexagonal architecture.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures CORS mappings for the application.
     *
     * @param registry the CORS registry
     * @throws RuntimeException if the CORS registry is null
     */
    @Override
    public void addCorsMappings(@Nullable CorsRegistry registry) {
        if (registry != null) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                    .allowedHeaders("Content-Type", "Date", "Total-Count", "loginInfo", "Authorization", "Token")
                    .allowCredentials(true);
        } else {
            throw new RuntimeException("CorsRegistry must not be null");
        }
    }

}