package org.example.bank_rest.properties;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.nio.charset.StandardCharsets;
import java.security.Key;


@ConfigurationProperties(prefix = "jwt")
@Getter
public class JwtProperties {
    private final String algorithm;
    private final TokenProperties accessToken;
    private final TokenProperties refreshToken;

    @ConstructorBinding
    public JwtProperties(String algorithm, TokenProperties accessToken, TokenProperties refreshToken) {
        this.algorithm = algorithm;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Getter
    public static class TokenProperties {

        private final Key signingKey;
        private final long expTime;

        @ConstructorBinding
        public TokenProperties(String secret, long expTime) {
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            this.expTime = expTime;
        }
    }
}