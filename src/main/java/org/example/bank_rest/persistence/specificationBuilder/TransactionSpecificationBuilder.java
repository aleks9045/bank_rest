package org.example.bank_rest.persistence.specificationBuilder;


import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest.persistence.model.entity.Card_;
import org.example.bank_rest.persistence.model.entity.Transaction;
import org.example.bank_rest.persistence.model.entity.Transaction_;
import org.example.bank_rest.persistence.model.entity.User_;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TransactionSpecificationBuilder extends JpaSpecificationBuilder<Transaction> {

    public Specification<Transaction> build(TransactionFilter filter) {

        return Specification.<Transaction>unrestricted()
            .and(this.greaterThan(Transaction_.AMOUNT, filter.getAmountMin()))
            .and(this.lessThan(Transaction_.AMOUNT, filter.getAmountMax()))
            .and(this.joinEqual(Transaction_.FROM_CARD, JoinType.INNER, Card_.ID, filter.getFromCardId()))
            .and(this.joinEqual(Transaction_.TO_CARD, JoinType.INNER, Card_.ID, filter.getToCardId()))
            .and(this.nestedJoinEqual(List.of(Transaction_.TO_CARD, Card_.OWNER), User_.UUID, filter.getSenderUuid()))
            .and(this.nestedJoinEqual(List.of(Transaction_.TO_CARD, Card_.OWNER), User_.UUID, filter.getReceiverUuid()));
    }

}
