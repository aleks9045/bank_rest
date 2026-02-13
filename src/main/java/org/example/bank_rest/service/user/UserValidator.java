
package org.example.bank_rest.service.user;

import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.RequestExceptionBuilder;
import org.example.bank_rest.exception.enums.NotFoundError;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.repository.UserRepository;
import org.example.bank_rest.service.validator.impl.EntityLookupValidatorImpl;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class UserValidator
    extends EntityLookupValidatorImpl<User, Long> {

    private final UserRepository userRepository;

    public UserValidator(UserRepository repository) {
        super(repository, new RequestExceptionBuilder(NotFoundError.NOT_FOUND_ERROR));
        this.userRepository = repository;
    }

    public User getUser(UUID uuid) {
        return userRepository.findByUuid(uuid)
            .orElseThrow(() -> this.requestExceptionBuilder.with(uuid));
    }

    public void checkExistenceByUuid(UUID uuid) {
        userRepository.findByUuid(uuid)
            .orElseThrow(() -> this.requestExceptionBuilder.with(uuid));
    }

    public User getUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        return (User) authentication.getPrincipal();
    }
}
