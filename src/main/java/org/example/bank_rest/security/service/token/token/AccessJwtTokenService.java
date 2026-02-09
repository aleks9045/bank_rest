package org.example.bank_rest.security.service.token.token;

import org.example.bank_rest.properties.JwtProperties;
import org.example.bank_rest.security.service.token.enums.JwtTokenType;
import org.example.bank_rest.security.service.token.impl.JwtTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;


@Component
public class AccessJwtTokenService extends JwtTokenServiceImpl {

    @Autowired
    protected AccessJwtTokenService(JwtProperties jwtProperties) {
        super(jwtProperties.getAccessToken().getSigningKey(),
                jwtProperties.getAccessToken().getExpTime());

    }

    public String generateAccessToken(String subject, Collection<?> roles) {
        var claims = new HashMap<String, Object>();
        if (roles != null) {
            claims.put(ROLES_CLAIM_NAME, roles);
        }
        claims.put(TOKEN_TYPE_CLAIM_NAME, JwtTokenType.ACCESS);

        return generateToken(subject, claims);
    }
}
