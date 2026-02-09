package org.example.bank_rest.security.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.JwtTokensDto;
import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserLoginDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.security.service.auth.AuthService;
import org.example.bank_rest.security.service.auth.AuthValidator;
import org.example.bank_rest.security.service.token.JwtTokenManager;
import org.example.bank_rest.service.user.UserService;
import org.example.bank_rest.service.user.UserValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * Authenticate and Authorize Service
 *
 * @author Aleksey
 */
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthValidator authValidator;
    private final UserValidator userValidator;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    @Override
    public UserViewDto saveUser(UserCreateDto userCreateDto) {
        authValidator.checkEmailDuplicates(userCreateDto.getEmail());

        authValidator.hashPassword(userCreateDto);
        return userService.saveUser(userCreateDto);
    }

    @Override
    public JwtTokensDto getTokens(UserLoginDto userLoginDto) {
        var user = authValidator.validateUserLoginDtoAndGetUser(userLoginDto);

        return new JwtTokensDto(
                jwtTokenManager.generateAccessToken(user.getUuid().toString(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()),
                jwtTokenManager.generateRefreshToken(user.getUuid().toString())
        );
    }

}
