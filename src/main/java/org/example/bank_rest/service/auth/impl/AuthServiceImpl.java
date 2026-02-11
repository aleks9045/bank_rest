package org.example.bank_rest.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.*;
import org.example.bank_rest.service.auth.AuthService;
import org.example.bank_rest.service.auth.AuthValidator;
import org.example.bank_rest.service.token.JwtTokenManager;
import org.example.bank_rest.service.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


/**
 * Authenticate and Authorize Service
 *
 * @author Aleksey
 */
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthValidator authValidator;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    @Override
    public UserViewDto saveUser(UserCreateDto userCreateDto) {
        authValidator.checkEmailDuplicates(userCreateDto.getEmail());

        authValidator.hashPassword(userCreateDto);
        userCreateDto.setRole(UserRoleDto.ROLE_USER);
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
