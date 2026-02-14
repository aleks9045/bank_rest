package org.example.bank_rest.persistence.specificationBuilder;

import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest.persistence.model.entity.Card;
import org.example.bank_rest.persistence.model.entity.Card_;
import org.example.bank_rest.persistence.model.entity.User_;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CardSpecificationBuilder extends JpaSpecificationBuilder<Card> {

    public Specification<Card> build(CardFilter filter) {

        return Specification.<Card>unrestricted()
            .and(this.joinEqual(Card_.OWNER, JoinType.INNER, User_.UUID, filter.getUserUuid()))
            .and(this.equal(Card_.STATUS, filter.getStatus()))
            .and(this.equal(Card_.NEED_TO_BLOCK, filter.getCardBlock()))
            .and(this.greaterThan(Card_.BALANCE, filter.getBalanceMin()))
            .and(this.lessThan(Card_.BALANCE, filter.getBalanceMax()));
    }

}
