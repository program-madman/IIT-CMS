package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "article_like")
public class ArticleLike implements Serializable {
    @Id
    @Column(name = "article_id")
    private Long articleId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "time", nullable = false)
    private Date time;

}

