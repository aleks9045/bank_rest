package org.example.bank_rest.security.service.token;


import io.jsonwebtoken.Claims;
import org.example.bank_rest.security.service.token.enums.JwtTokenType;

import java.util.List;
import java.util.Map;


public interface JwtTokenService {

    String generateToken(String subject, Map<String, Object> claims);

    String getSubject(String token);

    List<String> getRoles(String token);

    JwtTokenType getType(String token);

    JwtTokenType getType(Claims claims);

    boolean isTokenExpired(String token);

    boolean isTokenValid(String token, String subject);
}
