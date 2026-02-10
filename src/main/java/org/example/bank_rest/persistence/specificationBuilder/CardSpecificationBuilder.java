package org.example.bank_rest.persistence.specificationBuilder;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.persistence.model.entity.Card;
import org.example.bank_rest.persistence.model.entity.Card_;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.model.entity.User_;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CardSpecificationBuilder extends JpaSpecificationBuilder<User> {

    public Specification<Card> build(CardFilter filter) {

        return Specification.<Card>unrestricted();
    }

}
