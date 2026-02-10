package org.example.bank_rest.persistence.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "fromCard", "toCard"})
@ToString(exclude = {"fromCard", "toCard"})
public class Transaction {
    @Id
    @SequenceGenerator(name = "transactions_id_seq",
        sequenceName = "transactions_id_seq",
        allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_transaction_from_card")
    )
    private Card fromCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_transaction_to_card")
    )
    private Card toCard;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
