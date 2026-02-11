package org.example.bank_rest.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.enums.AuthError;
import org.example.bank_rest.persistence.repository.UserRepository;
import org.example.bank_rest.service.token.JwtTokenManager;
import org.example.bank_rest.service.token.enums.JwtTokenType;
import org.example.bank_rest.service.token.token.JwtAuthenticationToken;
import org.example.bank_rest.util.CookieUtil;
import org.example.bank_rest.util.JwtCookieManager;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.BooleanUtils.isFalse;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final JwtCookieManager jwtCookieManager;

    @Override

    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull  HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        var cookieMap = CookieUtil.getCookieMap(request);
        var accessToken = cookieMap.get(JwtTokenType.ACCESS.toString());
        var refreshToken = cookieMap.get(JwtTokenType.REFRESH.toString());

        if (accessToken == null || refreshToken == null) {
            // Sets anonymous user
            filterChain.doFilter(request, response);
            return;
        }
        var uuid = UUID.fromString(jwtTokenManager.getAccessTokenSubject(accessToken));

        var user = userRepository.findByUuid(uuid)
            .orElseThrow(() -> new RequestException(AuthError.INVALID_CREDENTIALS));

        var accessExpired = jwtTokenManager.isAccessTokenExpired(accessToken);
        var refreshExpired = jwtTokenManager.isRefreshTokenExpired(refreshToken);

        if (accessExpired && refreshExpired) {
            throw new RequestException(AuthError.TOKENS_EXPIRED);
        }

        if (accessExpired) {
            var newAccessToken = jwtTokenManager.generateAccessToken(uuid.toString(), List.of(user.getRole().toString()));
            var newAccessCookie = jwtCookieManager.createAccessCookie(newAccessToken);
            response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
            accessToken = newAccessToken;
            log.info("Access token for {} was updated", uuid);
        }

        if (isFalse(jwtTokenManager.isAccessTokenValid(accessToken, uuid.toString()) &&
            jwtTokenManager.isRefreshTokenValid(refreshToken, uuid.toString())))
            throw new RequestException(AuthError.INVALID_CREDENTIALS);

        SecurityContextHolder.getContext().setAuthentication(
            new JwtAuthenticationToken(user, user.getAuthorities()));

        filterChain.doFilter(request, response);
    }

}
