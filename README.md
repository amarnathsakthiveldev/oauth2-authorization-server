# OAuth2 Authorization Server

A Spring Boot based OAuth2 Authorization Server that generates JWT Bearer tokens for securing downstream APIs.

This service acts as a central authentication server. Other microservices can validate the JWT access tokens issued by this service.

---

## Architecture

```
                         +----------------+
                         |  Client App    |
                         | (Service/API)  |
                         +-------+--------+
                                 |
                                 | Client ID + Secret
                                 |
                                 v
                    +---------------------------+
                    | OAuth2 Authorization      |
                    | Server                    |
                    | Spring Boot               |
                    | Port: 9000                |
                    +-------------+-------------+
                                  |
                                  |
                                  | JWT Access Token
                                  |
                                  v

              +--------------------------------+
              | Resource Servers               |
              |                                |
              | customer-service               |
              | order-service                  |
              | payment-service                |
              +--------------------------------+

              Authorization:
              Bearer <JWT Token>
```

---

# Technology Stack

- Java 21
- Spring Boot 4.1.x
- Spring Security 7.x
- Spring Authorization Server
- OAuth2 Client Credentials Flow
- JWT Token

---

# OAuth2 Flow

This implementation uses **Client Credentials Grant**.

Flow:

```
Client
  |
  | 1. Request token
  |    client_id + client_secret
  |
  v
Authorization Server

  |
  | 2. Generate JWT
  |
  v

Client

  |
  | 3. Call API
  |    Authorization: Bearer JWT
  |
  v

Resource Server
```

---

# Project Structure

```
oauth2-authorization-server
|
├── src/main/java
│
├── config
│   ├── SecurityConfig.java
│   ├── ClientConfig.java
│   └── JwkConfig.java
│
└── Oauth2AuthorizationServerApplication.java
```

---

# Configuration

## Server Port

`application.yml`

```yaml
server:
  port: 9000
```

---

## OAuth2 Issuer

```yaml
spring:
  security:
    oauth2:
      authorizationserver:
        issuer: http://localhost:9000
```

The issuer value becomes the JWT `iss` claim.

Example:

```json
{
  "iss": "http://localhost:9000",
  "sub": "my-client",
  "scope": "api.read"
}
```

---

# Registered Client

Example client:

```
Client ID:
my-client

Client Secret:
my-secret

Grant Type:
client_credentials

Scope:
api.read
```

---

# Generate Access Token

Request:

```bash
curl -X POST \
http://localhost:9000/oauth2/token \
-u my-client:my-secret \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "grant_type=client_credentials&scope=api.read"
```

---

## Token Response

Example:

```json
{
  "access_token": "eyJhbGciOiJSUzI1Ni...",
  "token_type": "Bearer",
  "expires_in": 299,
  "scope": "api.read"
}
```

---

# JWT Validation

The Authorization Server exposes public keys:

```
GET

http://localhost:9000/oauth2/jwks
```

Resource servers use this public key to validate JWT signatures.

---

# Resource Server Configuration

Any Spring Boot API can validate tokens using:

`application.yml`

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9000
```

Example:

```
customer-service
       |
       |
       | Validate JWT
       |
       v
oauth2-authorization-server
```

---

# Calling Protected API

Request:

```bash
curl \
http://localhost:8081/customer \
-H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

# OAuth2 Endpoints

| Endpoint | Purpose |
|---|---|
| `/oauth2/token` | Generate access token |
| `/oauth2/jwks` | Public key endpoint |
| `/oauth2/introspect` | Token validation |
| `/oauth2/revoke` | Revoke token |
| `/.well-known/oauth-authorization-server` | Metadata |

---

# Security Flow

Token generation:

```
Client ID
    +
Client Secret
    |
    v
Authorization Server
    |
    v
JWT Token
```

API authentication:

```
Request
   |
   |
Bearer JWT
   |
   v
Resource Server
   |
   |
Validate:
 - Signature
 - Expiry
 - Issuer
 - Scope
```

---

# Production Recommendations

## Store Client Information in Database

Replace:

```
InMemoryRegisteredClientRepository
```

with:

```
JdbcRegisteredClientRepository
```

---

## Store RSA Keys Securely

Do not generate keys on every restart.

Use:

- Azure Key Vault
- AWS KMS
- Hashicorp Vault
- Java Keystore

---

## Use HTTPS

Production:

```
https://auth.company.com
```

instead of:

```
http://localhost:9000
```

---

# Future Enhancements

- User login flow (Authorization Code + PKCE)
- Refresh tokens
- Role based authorization
- Database backed clients
- Token customization
- Multi-tenant support
- Audit logging

---

# Local Development

Start application:

```bash
mvn spring-boot:run
```

Application:

```
http://localhost:9000
```

Generate token:

```bash
curl -X POST \
http://localhost:9000/oauth2/token \
-u my-client:my-secret \
-d "grant_type=client_credentials"
```