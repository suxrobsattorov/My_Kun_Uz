package com.company.repository;

import com.company.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer>,
        JpaSpecificationExecutor<ArticleEntity> {

    @Modifying
    @Transactional
    @Query("UPDATE ArticleEntity a SET a.title = ?2 WHERE a.id = ?1")
    void updateTitleById(Integer id, String title);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleEntity a SET a.content = ?2 WHERE a.id = ?1")
    void updateContentById(Integer id, String content);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleEntity a SET a.profile.id = ?2 WHERE a.id = ?1")
    void updateProfileIdById(Integer id, Integer pid);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleEntity a SET a.publisher.id = ?2 WHERE a.id = ?1")
    void updatePublisherIdById(Integer id, Integer pid);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleEntity a SET a.publishedDate = ?2 WHERE a.id = ?1")
    void updatePublishedDateById(Integer id, LocalDateTime publisheDate);

}
