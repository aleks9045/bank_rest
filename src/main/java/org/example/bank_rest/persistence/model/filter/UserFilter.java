package org.example.bank_rest.persistence.model.filter;

import lombok.Builder;

@Builder
public record UserFilter(
        String email,
        String firstName,
        String lastName
) { }
