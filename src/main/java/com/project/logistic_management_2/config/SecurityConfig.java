package com.project.logistic_management_2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenProvider;
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("*"));
                    configuration.setAllowedMethods(Arrays.asList("*"));
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    return configuration;
                }))
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/auth/**").permitAll() // Cho phép truy cập không xác thực đến /auth/**
                                .requestMatchers("/uploads/**").permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");

                            String jsonResponse = (new ObjectMapper()).writeValueAsString(
                                    BaseResponse.fail("Access is not allowed!")
                            );
                            response.getWriter().write(jsonResponse);
                        })
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

