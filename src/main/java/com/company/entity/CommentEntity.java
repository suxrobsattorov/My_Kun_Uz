package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "comment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseEntity{

    private String content;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

}
