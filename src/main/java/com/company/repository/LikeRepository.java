package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.LikeEntity;
import com.company.enums.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    int countByArticle_IdAndStatus(Integer articleId, LikeStatus status);

    int countByComment_IdAndStatus(Integer commentId, LikeStatus status);

    @Query("SELECT l.article FROM likes l WHERE l.profile.id = ?1")
    List<ArticleEntity> findLikedArticlesByPid(Integer profileId);

    @Query("SELECT l.comment FROM likes l WHERE l.profile.id = ?1")
    List<CommentEntity> findLikedCommentsByPid(Integer profileId);


}
