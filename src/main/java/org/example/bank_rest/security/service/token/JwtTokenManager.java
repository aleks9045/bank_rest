package org.example.bank_rest.security.service.token;



import org.example.bank_rest.security.service.token.enums.JwtTokenType;

import java.util.Collection;
import java.util.List;


public interface JwtTokenManager {

    String generateAccessToken(String subject, Collection<?> role);

    String generateRefreshToken(String subject);

    boolean isAccessTokenValid(String token, String subject);

    boolean isRefreshTokenValid(String token, String subject);

    boolean isAccessTokenExpired(String token);

    boolean isRefreshTokenExpired(String token);

    String getAccessTokenSubject(String token);

    String getRefreshTokenSubject(String token);

    List<String> getRoles(String token);

    JwtTokenType getAccessTokenType(String token);

    JwtTokenType getRefreshTokenType(String token);
}
