package org.example.bank_rest.persistence.model.listener;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.bank_rest.persistence.model.entity.Timestamps;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
public class TimestampsListener {

    @PrePersist
    protected void onCreate(Timestamps timestamps) {
        var now = OffsetDateTime.now();
        timestamps.setCreatedAt(now);
        timestamps.setUpdatedAt(now);
    }

    @PreUpdate
    protected void onUpdate(Timestamps timestamps) {
        timestamps.setUpdatedAt(OffsetDateTime.now());
    }
}
