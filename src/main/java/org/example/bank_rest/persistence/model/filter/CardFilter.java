package org.example.bank_rest.persistence.model.filter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.bank_rest.dto.CardStatusDto;

import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class CardFilter {
    private UUID userUuid;
    private CardStatusDto status;
}
