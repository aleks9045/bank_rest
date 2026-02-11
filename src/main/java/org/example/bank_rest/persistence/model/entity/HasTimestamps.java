package org.example.bank_rest.persistence.model.entity;

import java.time.OffsetDateTime;

public interface HasTimestamps {

    void setCreatedAt(OffsetDateTime createdAt);

    void setUpdatedAt(OffsetDateTime updatedAt);

}
