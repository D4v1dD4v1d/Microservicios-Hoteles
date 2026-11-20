package com.david.gateway.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                })).authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .pathMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .pathMatchers(
                                "/api/*/swagger-ui.html",
                                "/api/*/swagger-ui/**",
                                "/api/*/v3/api-docs/**"
                        ).permitAll()
                        .pathMatchers("/api/habitacion/id-habitacion/**", "/api/habitaciones/id-habitacion/**",
                                "/api/huesped/id-huesped/**", "/api/huespedes/id-huesped/**",
                                "/api/reservas/id-habitacion/**", "/api/reservas/id-huesped/**",
                                "/api/huesped/*/tipoDocumento/**", "/api/huespedes/*/tipoDocumento/**").denyAll()
                        .pathMatchers("/api/reservas/reportes/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/habitacion/**", "/api/habitaciones/**").hasAnyRole("ADMIN", "USER")
                        .pathMatchers("/api/habitacion/**", "/api/habitaciones/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.PATCH, "/**").hasAnyRole("ADMIN", "USER")
                        .anyExchange().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter())));

        return http.build();

    }

    @Bean
    ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}