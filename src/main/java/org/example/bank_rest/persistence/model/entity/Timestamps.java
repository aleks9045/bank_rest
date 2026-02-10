package org.example.bank_rest.persistence.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.bank_rest.persistence.model.listener.TimestampsListener;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@EntityListeners(TimestampsListener.class)
@Embeddable
public class Timestamps {

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private OffsetDateTime updatedAt;

}

