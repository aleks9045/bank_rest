package org.example.bank_rest.persistence.repository;


import org.example.bank_rest.persistence.model.entity.Card;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface CardRepository extends
        JpaRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {

    @Override
    @NullMarked
    Page<Card> findAll(Specification<Card> spec, Pageable pageable);

    @EntityGraph("withOwner")
    Optional<Card> findWithOwnerById(Long id);

    @Modifying
    @Query("""
        update Card c
        set c.balance = c.balance - :amount
        where c.id = :cardId
        and c.balance >= :amount
    """)
    int withdrawIfEnough(Long cardId, BigDecimal amount);

    @Modifying
    @Query("""
        update Card c
        set c.balance = c.balance + :amount
        where c.id = :cardId
    """)
    int deposit(Long cardId, BigDecimal amount);
}
