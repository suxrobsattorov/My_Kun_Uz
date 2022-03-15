package com.company.repository;

import com.company.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE region r SET r.name = ?2 WHERE r.id = ?1")
    void updateNameById(Integer id, String name);

}
