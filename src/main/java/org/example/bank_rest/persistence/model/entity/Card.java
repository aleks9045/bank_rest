package org.example.bank_rest.persistence.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bank_rest.persistence.model.entity.enums.CardStatus;
import org.example.bank_rest.persistence.model.entity.enums.CardType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cards")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "owner"})
@ToString(exclude = {"owner"})
public class Card {

    @Id
    @SequenceGenerator(name = "cards_id_seq",
        sequenceName = "cards_id_seq",
        allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "balance", scale = 12, precision = 2)
    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private CardStatus status;

    @Column(name = "type", precision = 16)
    private CardType type;

    @Column(name = "last_four_gigits", precision = 4)
    private Integer lastFourDigits;

    @Column(name = "number")
    private String encryptedNumber;

    @Column(name = "expiry_month", precision = 2)
    private Integer expiryMonth;

    @Column(name = "expiry_year", precision = 4)
    private Integer expiryYear;
}
