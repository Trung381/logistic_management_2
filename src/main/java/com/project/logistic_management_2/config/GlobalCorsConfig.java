package com.project.logistic_management_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOriginPattern("http://localhost:3000"); // Chấp nhận từ origin cụ thể
        config.addAllowedOriginPattern("*"); // Cho phép tất cả origin
        config.addAllowedMethod("*"); // Chấp nhận tất cả các phương thức (GET, POST,...)
        config.addAllowedHeader("*"); // Chấp nhận tất cả các headers
        config.setAllowCredentials(true); // Cho phép credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

