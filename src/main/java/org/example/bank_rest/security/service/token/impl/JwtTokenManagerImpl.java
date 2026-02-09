package org.example.bank_rest.security.service.token.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.security.service.token.JwtTokenManager;
import org.example.bank_rest.security.service.token.enums.JwtTokenType;
import org.example.bank_rest.security.service.token.token.AccessJwtTokenService;
import org.example.bank_rest.security.service.token.token.RefreshJwtTokenService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Stores two objects for separation and unification methods
 * @author Aleksey
 */
@Component
@RequiredArgsConstructor
public class JwtTokenManagerImpl implements JwtTokenManager {

    private final AccessJwtTokenService accessTokenService;
    private final RefreshJwtTokenService refreshTokenService;

    public String generateAccessToken(String subject, Collection<?> roles) {
        return accessTokenService.generateAccessToken(subject, roles);
    }

    public String generateRefreshToken(String subject) {
        return refreshTokenService.generateRefreshToken(subject);
    }

    public boolean isAccessTokenValid(String token, String subject) {
        return accessTokenService.isTokenValid(token, subject);
    }

    public boolean isRefreshTokenValid(String token, String subject) {
        return refreshTokenService.isTokenValid(token, subject);
    }

    @Override
    public boolean isAccessTokenExpired(String token) {
        return accessTokenService.isTokenExpired(token);
    }

    @Override
    public boolean isRefreshTokenExpired(String token) {
        return refreshTokenService.isTokenExpired(token);
    }

    @Override
    public String getAccessTokenSubject(String token) {
        return accessTokenService.getSubject(token);
    }

    @Override
    public String getRefreshTokenSubject(String token) {
        return refreshTokenService.getSubject(token);
    }

    @Override
    public List<String> getRoles(String token) {
        return accessTokenService.getRoles(token);
    }


    @Override
    public JwtTokenType getAccessTokenType(String token) {
        return accessTokenService.getType(token);
    }

    @Override
    public JwtTokenType getRefreshTokenType(String token) {
        return refreshTokenService.getType(token);
    }

}

