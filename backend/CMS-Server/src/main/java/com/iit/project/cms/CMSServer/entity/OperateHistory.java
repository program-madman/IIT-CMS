package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "operate_history")
public class OperateHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operate_id")
    private Long operateId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "operate_type", nullable = false)
    private String operateType;

    @Column(name = "time", nullable = false)
    private Date time;
}
