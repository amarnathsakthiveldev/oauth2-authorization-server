package com.oauth2.authorization.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configuration class for setting up security in the OAuth2 authorization server.
 * It defines two security filter chains:
 * 1. The first filter chain (with order 1) is responsible for handling requests to the OAuth2 authorization server endpoints.
 * 2. The second filter chain (with order 2) is responsible for handling other requests, including form login and access to well-known endpoints.
 */
@Configuration
public class SecurityConfig {


    /**
     * Defines a bean for the SecurityFilterChain that handles requests to the OAuth2 authorization server endpoints.
     * This filter chain is configured with the OAuth2AuthorizationServerConfigurer, which sets up the necessary security configurations for the authorization server.
     * The securityMatcher is used to match requests to the authorization server endpoints, and the Customizer.withDefaults() method applies default configurations.
     * 
     * @param http The HttpSecurity object used to configure security settings.
     * @return A SecurityFilterChain that handles requests to the OAuth2 authorization server endpoints.
     * @throws Exception If an error occurs while configuring security settings.
     */
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {


        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();


        http
            .securityMatcher(
                authorizationServerConfigurer.getEndpointsMatcher()
            )
            .with(
                authorizationServerConfigurer,
                Customizer.withDefaults()
            );


        return http.build();
    }


    /**
     * Defines a bean for the SecurityFilterChain that handles other requests, including form login and access to well-known endpoints.
     * This filter chain is configured to permit access to the /.well-known/** and /oauth2/jwks endpoints, while requiring authentication for all other requests.
     * The formLogin method is used to enable form-based login with default configurations.
     * 
     * @param http The HttpSecurity object used to configure security settings.
     * @return A SecurityFilterChain that handles other requests, including form login and access to well-known endpoints.
     * @throws Exception If an error occurs while configuring security settings.
     */
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http) throws Exception {


        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/.well-known/**",
                    "/oauth2/jwks"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());


        return http.build();
    }
}