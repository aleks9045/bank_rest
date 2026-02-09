package org.example.bank_rest.persistence.model.filter;

import java.util.UUID;


public record WorkspaceFilter(
        String name,
        UUID uuid,
        Long projectId
) {}
