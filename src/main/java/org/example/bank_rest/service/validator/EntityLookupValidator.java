package org.example.bank_rest.service.validator;


public interface EntityLookupValidator<T, ID> {

    T getById(ID id);

    void checkExistence(ID id);
}
