package org.example.bank_rest.mapper;

import org.example.bank_rest.persistence.model.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardIdMapper {
    public Long toId(Card entity) {
        return entity.getId();
    }

    public Card toEntity(Long id) {
        var card = new Card();
        card.setId(id);
        return card;
    }
}
