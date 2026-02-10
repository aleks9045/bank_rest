package org.example.bank_rest.persistence.repository;


import org.example.bank_rest.persistence.model.entity.Card;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CardRepository extends
        JpaRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {

    @Override
    Page<Card> findAll(Specification<Card> spec, Pageable pageable);
}
