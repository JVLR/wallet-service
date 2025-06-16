package com.wallet.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.render.AccessRefreshToken;
import jakarta.inject.Inject;

import java.util.Map;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/auth")
public class AuthController {

    @Inject
    JwtTokenGenerator jwtGenerator;

    @Post("/login")
    @Secured(IS_ANONYMOUS)
    public AccessRefreshToken login(@Body UsernamePasswordCredentials credentials) {
        //we can save the plain text password in a secure way using parameter store, but for this example we will use a hardcoded value
        if ("admin".equals(credentials.getUsername()) && "recpayPass123@".equals(credentials.getPassword())) {
            return jwtGenerator.generateToken(Map.of("sub", credentials.getUsername()))
                    .map(token -> new AccessRefreshToken(token, null, null, null))
                    .orElseThrow();
        }
        throw new AuthenticationException(new AuthenticationFailed());
    }
}