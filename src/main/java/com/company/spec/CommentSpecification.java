package com.company.spec;

import com.company.entity.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class CommentSpecification {

    public static Specification<CommentEntity> idIsNotNull(String field) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get(field)));
    }

    public static Specification<CommentEntity> equal(String field, Object o) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field), o);
    }

    public static Specification<CommentEntity> fromDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
                        LocalDateTime.of(date, LocalTime.MIN));
    }

    public static Specification<CommentEntity> toDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
                        LocalDateTime.of(date, LocalTime.MAX));
    }

}
