package org.example.bank_rest.controller;


import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.AuthApi;
import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserLoginDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.service.auth.AuthService;
import org.example.bank_rest.util.JwtCookieManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final JwtCookieManager authCookieFabric;
    private final AuthService authService;

    @Override
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> login(UserLoginDto userLoginDto) {

        var tokens = authService.getTokens(userLoginDto);

        var accessCookie = authCookieFabric.createAccessCookie(tokens.getAccess());
        var refreshCookie = authCookieFabric.createRefreshCookie(tokens.getRefresh());

        var headers = authCookieFabric.getCookiesInHeaders(List.of(accessCookie, refreshCookie));

        return ResponseEntity.ok().headers(headers).build();
    }

    @Override
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<UserViewDto> register(UserCreateDto userCreateDto) {
        var savedUser = authService.saveUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout() {
        var headers = authCookieFabric.deleteCookiesInHeaders();
        return ResponseEntity.ok().headers(headers).build();
    }

}
