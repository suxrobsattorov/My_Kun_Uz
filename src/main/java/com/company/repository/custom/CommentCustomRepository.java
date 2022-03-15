package com.company.repository.custom;

import com.company.dto.filterDTO.CommentFilterDTO;
import com.company.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<CommentEntity> filter(CommentFilterDTO dto, Pageable pageable){
        Map<String, Object> params = new HashMap<>();

        StringBuilder jpql = new StringBuilder("SELECT c FROM comment c WHERE c.id > 0 ");
        StringBuilder count = new StringBuilder("SELECT COUNT(c.id) FROM comment c WHERE c.id > 0 ");

        if (dto.getId() != null) {
            jpql.append("AND c.id = :id");
            count.append("AND c.id = :id");
            params.put("id", dto.getId());
        }
        if (dto.getProfileId() != null) {
            jpql.append("AND c.profile.id = :pid");
            count.append("AND c.profile.id = :pid");
            params.put("pid", dto.getProfileId());
        }
        if (dto.getArticleId() != null) {
            jpql.append("AND c.article.id = :aid");
            count.append("AND c.article.id = :aid");
            params.put("aid", dto.getArticleId());
        }
        if (dto.getFromDate() != null){
            jpql.append("AND c.createdDate >= :fromDate ");
            count.append("AND c.createdDate >= :fromDate ");
            params.put("fromDate", LocalDateTime.of(dto.getFromDate(), LocalTime.MIN));
        }
        if (dto.getToDate() != null){
            jpql.append("AND c.createdDate <= :toDate ");
            count.append("AND c.createdDate <= :toDate ");
            params.put("toDate", LocalDateTime.of(dto.getToDate(), LocalTime.MAX));
        }
        if (dto.getOrderBy() != null){
            jpql.append("ORDER BY c.").append(dto.getOrderBy().getQuery());
        }

        Query countQuery = entityManager.createQuery(count.toString());
        Query getQuery = entityManager.createQuery(jpql.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()){
            getQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        long totalElements = (long) countQuery.getSingleResult();

        getQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        getQuery.setMaxResults(pageable.getPageSize());

        List<CommentEntity> list = getQuery.getResultList();

        return new PageImpl<>(list, pageable, totalElements);
    }
}
