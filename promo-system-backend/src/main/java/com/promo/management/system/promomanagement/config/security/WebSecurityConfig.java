package com.promo.management.system.promomanagement.config.security;


import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyAuthority;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] PATTERN_EXCLUDE = new String[] {
        "/actuator/**"
    };

    private static final String[] PATTERN_ADMIN = new String[] {
        "/admin/**"
    };

    private static final String[] PATTERN_PROMO = new String[] {
        "/promo/**"
    };

    private static final String ADMIN_AUTHORITY = "ADMIN";
    private static final String BUSINESS_AUTHORITY = "BUSINESS";

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity,
                                           final JwtDecoder jwtDecoder,
                                           final JwtBearerTokenAuthenticationConverter jwtBearerTokenAuthenticationConverter) throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers(PATTERN_EXCLUDE).permitAll()
                    .requestMatchers(PATTERN_ADMIN).access(hasAuthority(ADMIN_AUTHORITY))
                    .requestMatchers(PATTERN_PROMO).access(hasAnyAuthority(ADMIN_AUTHORITY, BUSINESS_AUTHORITY))
                    .anyRequest().authenticated())
            .oauth2ResourceServer(configure -> configure
                .jwt(
                    jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(jwtBearerTokenAuthenticationConverter)
                        .decoder(jwtDecoder)
                )
            )
            .build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
