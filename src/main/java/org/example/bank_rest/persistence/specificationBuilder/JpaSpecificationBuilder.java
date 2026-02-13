package org.example.bank_rest.persistence.specificationBuilder;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds {@link Specification} object using lambda function and criteria builder

 */
public class JpaSpecificationBuilder<T> {

    public Specification<T> equal(String fieldName, Object value) {
        if (value == null) return Specification.unrestricted();
        return (root, query, criteriaBuilder) -> {

            if (value instanceof String) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(fieldName)),
                        ((String) value).toLowerCase());
            }
            return criteriaBuilder.equal(root.get(fieldName), value);
        };
    }

    public Specification<T> dateTimeFrom(String fieldName, Instant value) {
        if (value == null) return Specification.unrestricted();
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get(fieldName), value));
    }

    public Specification<T> dateTimeTo(String fieldName, Instant value) {
        if (value == null) return Specification.unrestricted();
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get(fieldName), value));
    }
    public <Y extends Comparable<? super Y>> Specification<T> greaterThan(String fieldName,Y value) {
        if (value == null) return Specification.unrestricted();
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(fieldName), value);
    }

    public <Y extends Comparable<? super Y>> Specification<T> lessThan(String fieldName, Y value) {
        if (value == null) return Specification.unrestricted();
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(fieldName), value);
    }

    public Specification<T> like(String fieldName, String value) {
        if (value == null || value.isBlank()) return Specification.unrestricted();

        return (root, query, cb) ->
                cb.like(cb.lower(root.get(fieldName).as(String.class)), "%" + value.toLowerCase() + "%");
    }

    public Specification<T> joinEqual(String joinFieldName, JoinType joinType,
                                      String comparisonFieldName,
                                      Object value) {
        if (value == null) return Specification.unrestricted();
        return (root, query, criteriaBuilder) -> {
            Join<?, ?> join = root.join(joinFieldName, joinType);
            return criteriaBuilder.equal(join.get(comparisonFieldName), value);
        };
    }

    public Specification<T> nestedJoinEqual(List<String> joinFields,
                                            String comparisonField,
                                            Object value) {
        if (value == null) return Specification.unrestricted();
        return (root, query, criteriaBuilder) -> {
            var mutableJoinFields = new ArrayList<>(joinFields);
            Join<?, ?> currentJoin = root.join(mutableJoinFields.getFirst());
            mutableJoinFields.removeFirst();

            for (String joinField : mutableJoinFields) {
                currentJoin = currentJoin.join(joinField, JoinType.INNER);
            }

            return criteriaBuilder.equal(currentJoin.get(comparisonField), value);
        };
    }

}
