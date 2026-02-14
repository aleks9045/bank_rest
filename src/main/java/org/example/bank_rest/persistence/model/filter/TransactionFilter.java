package org.example.bank_rest.persistence.model.filter;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilter {
    private Long fromCardId;
    private Long toCardId;
    private UUID senderUuid;
    private UUID receiverUuid;
    private BigDecimal amountMin;
    private BigDecimal amountMax;
}
