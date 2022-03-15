package com.company.repository.custom;

import com.company.dto.filterDTO.ProfileFilterDTO;
import com.company.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
public class ProfileCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<ProfileEntity> filter(ProfileFilterDTO dto, Pageable pageable){

        Map<String, Object> params = new HashMap<>();

        StringBuilder countBuilder =
                new StringBuilder("SELECT count(p) FROM ProfileEntity p WHERE p.id > 0 ");
        StringBuilder queryBuilder =
                new  StringBuilder("SELECT p FROM ProfileEntity p WHERE p.id > 0 ");

        if (dto.getId() != null){
            countBuilder.append("AND p.id = :id ");
            queryBuilder.append("AND p.id = :id ");
            params.put("id", dto.getId());
        }
        if (dto.getName() != null){
            countBuilder.append("AND p.name = :name ");
            queryBuilder.append("AND p.name = :name ");
            params.put("name", dto.getName());
        }
        if (dto.getSurname() != null){
            countBuilder.append("AND p.surname = :surname ");
            queryBuilder.append("AND p.surname = :surname ");
            params.put("surname", dto.getSurname());
        }
        if (dto.getEmail() != null){
            countBuilder.append("AND p.email = :email ");
            queryBuilder.append("AND p.email = :email ");
            params.put("email", dto.getEmail());
        }
        if (dto.getRole() != null){
            countBuilder.append("AND p.role = :role ");
            queryBuilder.append("AND p.role = :role ");
            params.put("role", dto.getRole());
        }
        if (dto.getStatus() != null){
            countBuilder.append("AND p.status = :status ");
            queryBuilder.append("AND p.status = :status ");
            params.put("status", dto.getStatus());
        }
        if (dto.getFromDate() != null){
            countBuilder.append("AND p.createdDate >= :fromDate ");
            queryBuilder.append("AND p.createdDate >= :fromDate ");
            params.put("fromDate", LocalDateTime.of(dto.getFromDate(), LocalTime.MIN));
        }
        if (dto.getToDate() != null){
            countBuilder.append("AND p.createdDate <= :toDate ");
            queryBuilder.append("AND p.createdDate <= :toDate ");
            params.put("toDate", LocalDateTime.of(dto.getToDate(), LocalTime.MAX));
        }

        if (dto.getOrderBy() != null){
            queryBuilder.append("ORDER BY p.").append(dto.getOrderBy().getQuery());
        }

        Query countQuery = entityManager.createQuery(countBuilder.toString());
        Query query = entityManager.createQuery(queryBuilder.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()){
            countQuery.setParameter(entry.getKey(), entry.getValue());
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        long totalElements = (long) countQuery.getSingleResult();
        List<ProfileEntity> list = query.getResultList();

        return new PageImpl<ProfileEntity>(list, pageable, totalElements);
    }
}
