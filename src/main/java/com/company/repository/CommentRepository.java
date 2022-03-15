package com.company.repository;

import com.company.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>,
        JpaSpecificationExecutor<CommentEntity> {

    List<CommentEntity> findByProfile_Id(Integer profileId);

    Page<CommentEntity> findByArticle_Id(Integer articleId, Pageable pageable);

}
