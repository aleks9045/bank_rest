package org.example.bank_rest.security.service.auth;


import org.example.bank_rest.dto.JwtTokensDto;
import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserLoginDto;
import org.example.bank_rest.dto.UserViewDto;

public interface AuthService {

    UserViewDto saveUser(UserCreateDto userCreateDto);

    JwtTokensDto getTokens(UserLoginDto userLoginDto);

}
