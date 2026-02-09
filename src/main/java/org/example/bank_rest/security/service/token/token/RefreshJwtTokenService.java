package org.example.bank_rest.security.service.token.token;

import org.example.bank_rest.properties.JwtProperties;
import org.example.bank_rest.security.service.token.enums.JwtTokenType;
import org.example.bank_rest.security.service.token.impl.JwtTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class RefreshJwtTokenService extends JwtTokenServiceImpl {

    @Autowired
    protected RefreshJwtTokenService(JwtProperties jwtProperties) {
        super(jwtProperties.getRefreshToken().getSigningKey(),
                jwtProperties.getRefreshToken().getExpTime());

    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject,
                Map.of(
                        TOKEN_TYPE_CLAIM_NAME, JwtTokenType.REFRESH
                )
        );
    }
}
