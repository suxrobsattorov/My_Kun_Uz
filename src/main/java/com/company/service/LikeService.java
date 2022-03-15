package com.company.service;

import com.company.dto.LikeDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.LikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.exception.ItemNotFoundException;
import com.company.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    public LikeEntity toEntity(LikeDTO dto){
        LikeEntity entity = new LikeEntity();
        ProfileEntity profile = profileService.get(dto.getProfileId());

        ArticleEntity article = null;
        if (dto.getArticleId() != null)
            article = articleService.get(dto.getArticleId());

        CommentEntity comment = null;
        if (dto.getCommentId() != null)
            comment = commentService.get(dto.getCommentId());

        entity.setId(dto.getId());
        entity.setArticle(article);
        entity.setProfile(profile);
        entity.setComment(comment);
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    public LikeDTO toDto(LikeEntity entity){
        LikeDTO dto = new LikeDTO();

        dto.setId(entity.getId());
        dto.setArticleId(entity.getArticle().getId());
        dto.setProfileId(entity.getProfile().getId());
        dto.setCommentId(entity.getComment().getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public LikeDTO createLike(LikeDTO dto, Integer profileId){
        dto.setCreatedDate(LocalDateTime.now());
        dto.setProfileId(profileId);

        LikeEntity entity = toEntity(dto);
        likeRepository.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

    public void deleteById(Long id){
        likeRepository.deleteById(id);
    }

    public LikeEntity get(Long id){
        return likeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Like not Found"));
    }

    public void updateById(Long id, LikeDTO dto){
        LikeEntity entity = get(id);
        entity.setStatus(dto.getStatus());

        likeRepository.save(entity);
    }


    public int countArticleLikes(Integer articleId, LikeStatus status){
        return likeRepository.countByArticle_IdAndStatus(articleId, status);
    }

    public int countCommentLikes(Integer commentId, LikeStatus status){
        return likeRepository.countByComment_IdAndStatus(commentId, status);
    }

    public List<ArticleEntity> getLikedArticlesByPid(Integer pid){
        return likeRepository.findLikedArticlesByPid(pid);
    }

    public List<CommentEntity> getLikedCommentsByPid(Integer pid){
        return likeRepository.findLikedCommentsByPid(pid);
    }




}
