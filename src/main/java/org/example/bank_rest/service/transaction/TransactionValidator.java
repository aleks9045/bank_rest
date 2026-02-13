
package org.example.bank_rest.service.transaction;

import org.example.bank_rest.dto.TransactionCreateDto;
import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.exception.RequestExceptionBuilder;
import org.example.bank_rest.exception.enums.ConflictError;
import org.example.bank_rest.exception.enums.NotFoundError;
import org.example.bank_rest.persistence.model.entity.Transaction;
import org.example.bank_rest.persistence.model.entity.enums.CardStatus;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.persistence.repository.TransactionRepository;
import org.example.bank_rest.service.card.CardValidator;
import org.example.bank_rest.service.validator.impl.EntityLookupValidatorImpl;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.BooleanUtils.isFalse;


@Component
public class TransactionValidator
    extends EntityLookupValidatorImpl<Transaction, Long> {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final CardValidator cardValidator;

    public TransactionValidator(TransactionRepository repository, CardRepository cardRepository, CardValidator cardValidator) {
        super(repository, new RequestExceptionBuilder(NotFoundError.NOT_FOUND_ERROR));
        this.transactionRepository = repository;
        this.cardRepository = cardRepository;
        this.cardValidator = cardValidator;
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> this.requestExceptionBuilder.with(id));
    }

    public void makeMeansTransfer(TransactionCreateDto dto) {
        var fromCard = cardValidator.getCard(dto.getFromCardId());
        var toCard = cardValidator.getCard(dto.getToCardId());

        if (isFalse(fromCard.getStatus().equals(CardStatus.ACTIVE)))
            throw new RequestExceptionBuilder(ConflictError.INVALID_CARD_STATUS).with(fromCard.getId());
        if (isFalse(toCard.getStatus().equals(CardStatus.ACTIVE)))
            throw new RequestExceptionBuilder(ConflictError.INVALID_CARD_STATUS).with(toCard.getId());

        var updatedRows = cardRepository.withdrawIfEnough(
            dto.getFromCardId(),
            dto.getAmount()
        );
        if (updatedRows == 0) throw new RequestException(ConflictError.NOT_ENOUGH_MEANS);
        cardRepository.deposit(
            dto.getToCardId(),
            dto.getAmount()
        );
    }
}
