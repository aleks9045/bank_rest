package org.example.bank_rest.service.validator.impl;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.exception.RequestExceptionBuilder;
import org.example.bank_rest.service.validator.EntityLookupValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.apache.commons.lang3.BooleanUtils.isFalse;


@RequiredArgsConstructor
public abstract class EntityLookupValidatorImpl<T, ID> implements EntityLookupValidator<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final RequestExceptionBuilder requestExceptionBuilder;

    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> requestExceptionBuilder.with(id));
    }
    public void checkExistence(ID id) {
        if (isFalse(repository.existsById(id))) throw requestExceptionBuilder.with(id);
    }

}
