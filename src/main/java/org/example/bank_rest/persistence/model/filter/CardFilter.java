package org.example.bank_rest.persistence.model.filter;


import lombok.*;
import org.example.bank_rest.dto.CardStatusDto;

import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardFilter {
    private UUID userUuid;
    private CardStatusDto status;
    private Boolean cardBlock;
}
