package com.oauth2.authorization.server.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwkConfig {

    /**
     * Defines a bean for the JWKSource, which is responsible for providing JSON Web
     * Keys (JWKs) used for signing and verifying JWTs.
     * In this example, an RSA key pair is generated and used to create a JWKSet.
     * The JWKSource is then configured to select keys from the JWKSet based on the
     * provided selector and context.
     * 
     * @return
     */
    @Bean
    JWKSource<SecurityContext> jwkSource() {

        RSAKey rsaKey = generateRsaKey();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return (selector, context) -> selector.select(jwkSet);

    }

    /**
     * Generates an RSA key pair for use in creating JSON Web Keys (JWKs).
     * The generated key pair consists of a public key and a private key, which are
     * used for signing and verifying JWTs.
     * The generated RSA key is then wrapped in an RSAKey object, which includes the
     * public key, private key, and a unique key ID.
     * 
     * @return
     */
    private RSAKey generateRsaKey() {

        try {

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

            generator.initialize(2048);

            KeyPair keyPair = generator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)

                    .keyID(UUID.randomUUID().toString())

                    .build();

        } catch (Exception e) {

            throw new IllegalStateException(e);

        }

    }

}
