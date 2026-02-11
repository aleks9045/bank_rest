package org.example.bank_rest.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserLoginDto;
import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.enums.AuthError;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.repository.UserRepository;
import org.example.bank_rest.service.auth.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.BooleanUtils.isFalse;


@Component
@RequiredArgsConstructor
public class AuthValidatorImpl implements AuthValidator {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private void checkPasswordsAreEqual(String password, String hashedPassword) {
        if (isFalse(passwordEncoder.matches(password, hashedPassword))) {
            throw new RequestException(AuthError.INVALID_CREDENTIALS);
        }
    }

    private void checkEmailsAreEqual(String email1, String email2) {
        if (isFalse(email1.equals(email2))) {
            throw new RequestException(AuthError.INVALID_CREDENTIALS);
        }
    }

    private User validateEmailAndGetUser(String email) {
        return userRepository.findFullByEmail(email)
                .orElseThrow(() -> new RequestException(AuthError.INVALID_CREDENTIALS));
    }

    @Override
    public User validateUserLoginDtoAndGetUser(UserLoginDto userLoginDto) {
        var user = validateEmailAndGetUser(userLoginDto.getEmail());

        checkPasswordsAreEqual(userLoginDto.getPassword(), user.getPassword());

        return user;
    }

    @Override
    public void checkEmailDuplicates(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RequestException(AuthError.EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public void hashPassword(UserCreateDto userCreateDto) {
        final var password = userCreateDto.getPassword();
        final var hashedPassword = passwordEncoder.encode(password);
        userCreateDto.setPassword(hashedPassword);
    }

}
