package com.luxiergerie.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@Nullable CorsRegistry registry) {
        if (registry != null) {
            registry.addMapping("/**")
                    .allowedOrigins("https://www.luxiergerie.tech", "http://localhost:8081")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                    .allowedHeaders("Content-Type", "Date", "Total-Count", "loginInfo", "Authorization", "Token")
                    .allowCredentials(true);
        } else {
            throw new RuntimeException("CorsRegistry must not be null");
        }
    }

}