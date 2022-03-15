package com.company.repository.custom;

import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public PageImpl<ArticleEntity> filter(Pageable pageable, ArticleFilterDTO dto){
        Map<String, Object> params = new HashMap<>();

        StringBuilder builder = new StringBuilder("SELECT a FROM ArticleEntity a WHERE id IS NOT NULL ");
        StringBuilder builder2 = new StringBuilder("SELECT count(a) FROM ArticleEntity a WHERE id IS NOT NULL ");
        if (dto.getId() != null){
            builder.append("AND a.id = :id ");
            builder2.append("AND a.id = :id ");
            params.put("id", dto.getId());
        }
        if (dto.getTitle() != null){
            builder.append("AND a.title = :title ");
            builder2.append("AND a.title = :title ");
            params.put("title", dto.getTitle());
        }
        if (dto.getProfileId() != null){
            builder.append("AND a.profile.id = :profileId ");
            builder2.append("AND a.profile.id = :profileId ");
            params.put("profileId", dto.getProfileId());
        }
        if (dto.getFromDate() != null){
            builder.append("AND a.publishedDate >= :fromDate ");
            builder2.append("AND a.publishedDate >= :fromDate ");
            params.put("fromDate", LocalDateTime.of(dto.getFromDate(), LocalTime.MIN));
        }
        if (dto.getToDate() != null){
            builder.append("AND a.publishedDate <= :toDate ");
            builder2.append("AND a.publishedDate <= :toDate ");
            params.put("toDate", LocalDateTime.of(dto.getToDate(), LocalTime.MAX));
        }

        Query count = entityManager.createQuery(builder2.toString());
        Query query = entityManager.createQuery(builder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()){
            query.setParameter(entry.getKey(), entry.getValue());
            count.setParameter(entry.getKey(), entry.getValue());
        }
        long totalElements = (long) count.getSingleResult();

        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<ArticleEntity> list = query.getResultList();

        return new PageImpl<ArticleEntity>(list, pageable, totalElements);
    }

    public List<ArticleEntity> filterCriteria(ArticleFilterDTO dto){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArticleEntity> criteriaQuery = criteriaBuilder.createQuery(ArticleEntity.class);
        Root<ArticleEntity> root = criteriaQuery.from(ArticleEntity.class);

        criteriaQuery.select(root);

        ArrayList<Predicate> predicateList = new ArrayList<>();

        if (dto.getId() != null){
            predicateList.add(criteriaBuilder.equal(root.get("id"), dto.getId()));
        }
        if (dto.getTitle() != null){
            predicateList.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
        }
        if (dto.getProfileId() != null){
            predicateList.add(criteriaBuilder.equal(root.get("profile.id"), dto.getId()));
        }
        if (dto.getFromDate() != null){
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), dto.getFromDate()));
        }
        if (dto.getToDate() != null){
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), dto.getToDate()));
        }
        if (dto.getStatus() != null){
            predicateList.add(criteriaBuilder
                    .lessThanOrEqualTo(root.get("status"), ArticleStatus.PUBLISHED));
        }

        Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);

        Predicate name = criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%");
        criteriaQuery
                .where(predicates)
                .orderBy(criteriaBuilder.desc(root.get("id")));

        List<ArticleEntity> list = entityManager.createQuery(criteriaQuery).getResultList();

        return list;
    }


}
