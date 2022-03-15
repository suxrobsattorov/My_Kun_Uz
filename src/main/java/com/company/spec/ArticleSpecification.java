package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ArticleSpecification {

    public static Specification<ArticleEntity> status(ArticleStatus status) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<ArticleEntity> title(String title) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("title"), title);
        });
    }

    public static Specification<ArticleEntity> equal(String field, Integer id) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field), id);
        });
    }

    public static Specification<ArticleEntity> equal(String field, Object o) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field), o);
        };
    }

    public static Specification<ArticleEntity> like(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get(field), "%" + value + "%");
        };
    }

    public static Specification<ArticleEntity> greaterThanOrEqualTo(String field, LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(field),
                    LocalDateTime.of(date, LocalTime.MIN));

    }

    public static Specification<ArticleEntity> lessThanOrEqualTo(String field, LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(field),
                        LocalDateTime.of(date, LocalTime.MAX));
    }

}
