
package org.example.bank_rest.service.card;

import org.example.bank_rest.exception.RequestExceptionBuilder;
import org.example.bank_rest.exception.enums.NotFoundError;
import org.example.bank_rest.persistence.model.entity.Card;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.service.validator.impl.EntityLookupValidatorImpl;
import org.springframework.stereotype.Component;


@Component
public class CardValidator
    extends EntityLookupValidatorImpl<Card, Long> {

    private final CardRepository cardRepository;

    public CardValidator(CardRepository repository) {
        super(repository, new RequestExceptionBuilder(NotFoundError.NOT_FOUND_ERROR));
        this.cardRepository = repository;
    }

    public Card getCard(Long id) {
        return cardRepository.findById(id)
            .orElseThrow(() -> this.requestExceptionBuilder.with(id));
    }

    public Card getCardWithOwner(Long id) {
        return cardRepository.findWithOwnerById(id)
            .orElseThrow(() -> this.requestExceptionBuilder.with(id));
    }
}
