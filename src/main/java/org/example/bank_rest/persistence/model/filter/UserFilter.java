package org.example.bank_rest.persistence.model.filter;

import jakarta.validation.constraints.Email;


public record UserFilter(
        String email,
        String firstName,
        String lastName
) { }
