
package org.example.bank_rest.service.card;

import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.enums.NotFoundError;
import org.example.bank_rest.persistence.model.entity.Card;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.service.validator.impl.EntityLookupValidatorImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CardValidator
    extends EntityLookupValidatorImpl<Card, Long> {

    private final CardRepository cardRepository;

    public CardValidator(CardRepository repository) {
        super(repository, new RequestException(NotFoundError.NOT_FOUND_ERROR));
        this.cardRepository = repository;
    }

    public Card getCard(Long id) {
        return cardRepository.findById(id)
            .orElseThrow(() -> this.notFoundException);
    }

    public void checkExistenceByUuid(Long id) {
        cardRepository.findById(id)
            .orElseThrow(() -> this.notFoundException);
    }
}
