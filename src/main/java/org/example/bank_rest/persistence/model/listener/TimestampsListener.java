package org.example.bank_rest.persistence.model.listener;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.bank_rest.persistence.model.entity.HasTimestamps;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
public class TimestampsListener {

    @PrePersist
    public void prePersist(HasTimestamps entity) {
        var now = OffsetDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(HasTimestamps entity) {
        entity.setUpdatedAt(OffsetDateTime.now());
    }
}
