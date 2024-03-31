package org.wooriverygood.api.global.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    @Value("${aws.cognito.uri}")
    private String uri;

    public JwtDecoder accessTokenDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(uri).build();
    }
}
