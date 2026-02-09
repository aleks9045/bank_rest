package org.example.bank_rest.persistence.model.listener;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.bank_rest.persistence.model.entity.TimeData;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
public class TimeDataListener {

    @PrePersist
    protected void onCreate(TimeData timeData) {
        var now = OffsetDateTime.now();
        timeData.setCreatedAt(now);
        timeData.setUpdatedAt(now);
    }

    @PreUpdate
    protected void onUpdate(TimeData timeData) {
        timeData.setUpdatedAt(OffsetDateTime.now());
    }
}
