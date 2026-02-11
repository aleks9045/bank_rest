package org.example.bank_rest.persistence.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.example.bank_rest.persistence.model.entity.enums.CardStatus;
import org.example.bank_rest.persistence.model.listener.TimestampsListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@EntityListeners(TimestampsListener.class)
@Table(name = "cards")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "owner"})
@ToString(exclude = {"owner"})
public class Card implements HasTimestamps{

    @Id
    @SequenceGenerator(name = "cards_id_seq",
        sequenceName = "cards_id_seq",
        allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id",
    nullable = false,
        foreignKey = @ForeignKey(name = "fk_cards_users"))
    private User owner;

    @Column(name = "balance", scale = 12, precision = 2)
    @Min(0)
    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private CardStatus status = CardStatus.ACTIVE;

    @Column(name = "last4", length = 4)
    private String last4;

    @Column(name = "encrypted_pan")
    private String encryptedPan;

    @Column(name = "expiry_month", precision = 2)
    @Min(1) @Max(12)
    private Integer expiryMonth;

    @Column(name = "expiry_year", precision = 4)
    @Min(0)
    private Integer expiryYear;

    @OneToMany(mappedBy = "fromCard")
    private List<Transaction> sent;

    @OneToMany(mappedBy = "toCard")
    private List<Transaction> received;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
