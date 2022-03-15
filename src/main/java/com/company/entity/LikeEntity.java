package com.company.entity;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "likes")
@Getter
@Setter
@NoArgsConstructor
public class LikeEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;


    @Enumerated(EnumType.STRING)
    private LikeStatus status;


}
