package org.example.bank_rest.persistence.repository;


import org.example.bank_rest.persistence.model.entity.User;
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
public interface UserRepository extends
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findFullByEmail(String email);

    void deleteByUuid(UUID uuid);

    boolean existsByEmail(String email);

    @Override
    Page<User> findAll(@Nullable Specification<User> spec, @NonNull Pageable pageable);
}
