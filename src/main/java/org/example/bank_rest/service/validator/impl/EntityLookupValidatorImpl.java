package org.example.bank_rest.service.validator.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.enums.NotFoundError;
import org.example.bank_rest.service.validator.EntityLookupValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.apache.commons.lang3.BooleanUtils.isFalse;


@RequiredArgsConstructor
public abstract class EntityLookupValidatorImpl<T, ID> implements EntityLookupValidator<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final RequestException notFoundException;

    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RequestException(NotFoundError.NOT_FOUND_ERROR));
    }
    public void checkExistence(ID id) {
        if (isFalse(repository.existsById(id))) throw new RequestException(NotFoundError.NOT_FOUND_ERROR);
    }

}
