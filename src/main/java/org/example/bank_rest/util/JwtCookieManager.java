package org.example.bank_rest.util;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.properties.JwtProperties;
import org.example.bank_rest.service.token.enums.JwtTokenType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtCookieManager {

    private final JwtProperties jwtProperties;

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from(JwtTokenType.ACCESS.toString(), token)
                .httpOnly(true)
                .maxAge(jwtProperties.getAccessToken().getExpTime())
                .path("/")
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from(JwtTokenType.REFRESH.toString(), token)
                .httpOnly(true)
                .maxAge(jwtProperties.getRefreshToken().getExpTime())
                .path("/")
                .sameSite("Lax")
                .build();
    }

    private ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .sameSite("Lax")
                .build();
    }

    public HttpHeaders deleteCookiesInHeaders() {

        var accessCookie = deleteCookie(JwtTokenType.ACCESS.toString());
        var refreshCookie = deleteCookie(JwtTokenType.REFRESH.toString());

        return getCookiesInHeaders(List.of(accessCookie, refreshCookie));
    }

    public HttpHeaders getCookiesInHeaders(Collection<ResponseCookie> cookies) {
        var httpHeaders = new HttpHeaders();
        cookies.forEach(cookie -> httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString()));
        return httpHeaders;
    }

    public HttpHeaders getCookieInHeader(ResponseCookie cookie) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return headers;
    }
}
