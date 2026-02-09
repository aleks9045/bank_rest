package org.example.bank_rest.security.service.token.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.example.bank_rest.security.service.token.JwtTokenService;
import org.example.bank_rest.security.service.token.enums.JwtTokenType;
import org.example.bank_rest.util.SaveCast;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;


public abstract class JwtTokenServiceImpl implements JwtTokenService {

    protected final String ROLES_CLAIM_NAME = "roles";
    protected final String TOKEN_TYPE_CLAIM_NAME = "token_type";

    private final Key signingKey;
    private final long expTime;

    protected JwtTokenServiceImpl(Key signingKey, long expTime) {
        this.signingKey = signingKey;
        this.expTime = expTime;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plus(expTime, ChronoUnit.SECONDS)))
                .signWith(signingKey)
                .compact();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith((SecretKey) signingKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    @Override
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public List<String> getRoles(String token) {
        Object rolesClaim = getClaims(token).get(ROLES_CLAIM_NAME);

        return SaveCast.toStringList(rolesClaim);
    }

    @Override
    public JwtTokenType getType(String token) {
        return JwtTokenType.valueOf(getClaims(token).get(TOKEN_TYPE_CLAIM_NAME).toString());
    }

    @Override
    public JwtTokenType getType(Claims claims) {
        return JwtTokenType.valueOf(claims.get(TOKEN_TYPE_CLAIM_NAME).toString());
    }

    private Date getExpTime(String token) {
        return getClaims(token).getExpiration();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return getExpTime(token).before(new Date());
    }


    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    @Override
    public boolean isTokenValid(String token, String subject) {
        Claims claims = getClaims(token);
        var currentSubject = claims.getSubject();
        return (currentSubject.equals(subject)) && !isTokenExpired(claims);
    }

}
