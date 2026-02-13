package org.example.bank_rest.persistence.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class TransactionFilter {
    private Long fromCardId;
    private Long toCardId;
    private UUID senderUuid;
    private UUID receiverUuid;
}
