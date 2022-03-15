package com.company.spec;

import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ProfileSpecification {

    public static Specification<ProfileEntity> idIsNotNull(){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("id"));
    }

    public static Specification<ProfileEntity> equal(String field, Object o){
        return (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(field), o);
    }

    public static Specification<ProfileEntity> fromDate(LocalDate date){
        return (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
                                    LocalDateTime.of(date, LocalTime.MIN));
    }

    public static Specification<ProfileEntity> toDate(LocalDate date){
        return (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
                                    LocalDateTime.of(date, LocalTime.MAX));
    }

    public static Specification<ProfileEntity> likeDouble(String field, String value){
        return (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get(field), "%" + value + "%");
    }



}
