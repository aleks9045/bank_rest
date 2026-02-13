package org.example.bank_rest.service.token.token;


import org.example.bank_rest.persistence.model.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Uses for SecurityContext

 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;

    public JwtAuthenticationToken(User principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }
}
