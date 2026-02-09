package org.example.bank_rest.security.service.auth;


import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserLoginDto;
import org.example.bank_rest.persistence.model.entity.User;

public interface AuthValidator {

    User validateUserLoginDtoAndGetUser(UserLoginDto userLoginDto);

    void checkEmailDuplicates(String email);

    void hashPassword(UserCreateDto userCreateDto);
}
