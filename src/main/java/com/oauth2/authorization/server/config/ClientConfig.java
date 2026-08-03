package com.oauth2.authorization.server.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;


/**
 * Configuration class for registering OAuth2 clients.
 * spring security oauth2 authorization server provides a default implementation of RegisteredClientRepository that stores client details in memory. This is suitable for development and testing purposes, but for production use, you may want to implement your own RegisteredClientRepository that retrieves client details from a database or another persistent storage.  
 */
@Configuration
public class ClientConfig {


    /**
     * Defines a bean for the RegisteredClientRepository, which is responsible for managing registered OAuth2 clients.
     * In this example, a single client is registered with the following details:
     * - Client ID: "my-client"
     * - Client Secret: "my-secret"
     * - Client Authentication Method: CLIENT_SECRET_BASIC
     * - Authorization Grant Type: CLIENT_CREDENTIALS
     * - Scope: "api.read"
     * @return
     */
    @Bean
    RegisteredClientRepository registeredClientRepository() {

        RegisteredClient client = RegisteredClient
                .withId(UUID.randomUUID().toString())

                .clientId("my-client")

                .clientSecret("{noop}my-secret")

                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS)

                .scope("api.read")

                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

}
