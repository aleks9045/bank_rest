package org.example.bank_rest.util;

import org.example.bank_rest.service.token.JwtTokenManager;
import org.example.bank_rest.service.token.enums.JwtTokenType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TokenGenerationTest {

    private final JwtTokenManager manager;

    @Autowired
    public TokenGenerationTest(JwtTokenManager manager) {
        this.manager = manager;
    }

    @Test
    public void testAccessTokenSubject() {

        var subject = "user";
        var token = manager.generateAccessToken(subject, List.of());

        assertEquals(manager.getAccessTokenSubject(token), subject);
    }

    @Test
    public void testRefreshTokenSubject() {

        var subject = "user";
        var token = manager.generateRefreshToken(subject);

        assertEquals(manager.getRefreshTokenSubject(token), subject);
    }

    @Test
    public void testAccessTokenRoles() {

        var roles = List.of("DEVELOPER", "TEAMLEAD");
        var token = manager.generateAccessToken(null, roles);

        assertEquals(manager.getRoles(token), roles);
    }


    @Test
    public void testAccessTokenType() {

        var token = manager.generateAccessToken(null, null);

        assertEquals(manager.getAccessTokenType(token), JwtTokenType.ACCESS);
    }

    @Test
    public void testRefreshTokenType() {

        var token = manager.generateRefreshToken(null);

        assertEquals(manager.getRefreshTokenType(token), JwtTokenType.REFRESH);
    }

}
