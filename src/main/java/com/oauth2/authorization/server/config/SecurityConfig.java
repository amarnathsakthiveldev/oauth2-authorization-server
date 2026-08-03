package com.oauth2.authorization.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up security for the OAuth2 authorization server.
 * It defines two security filter chains: one for the authorization server endpoints and another for application-level security.
 * The authorization server filter chain is configured to handle requests to the authorization server endpoints, while the application filter chain secures all other requests to the application.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines a bean for the SecurityFilterChain that handles security for the OAuth2 authorization server endpoints.
     * This filter chain is responsible for securing the authorization server endpoints, such as token issuance and revocation.
     * It uses the OAuth2AuthorizationServerConfigurer to configure the necessary security settings for the authorization server.
     * 
     * @param http the HttpSecurity object used to configure security settings
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security settings
     */
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(
                        authorizationServerConfigurer.getEndpointsMatcher())
                .with(
                        authorizationServerConfigurer,
                        Customizer.withDefaults());

        return http.build();
    }

    /**
     * Defines a bean for the SecurityFilterChain that handles application-level security.
     * This filter chain is responsible for securing all requests to the application, requiring authentication for any request.
     * It also enables form-based login for user authentication.
     * 
     * @param http the HttpSecurity object used to configure security settings
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security settings
     */
    @Bean
    @Order(2)
    SecurityFilterChain applicationSecurityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

}
