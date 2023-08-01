package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniel
 * @date 2023年07月31日 15:56
 */

@Data
@Entity
@Table(name = "browsed_history")
public class UserBrowse implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User userName;

    @Column(name = "time", nullable = false)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article categoryName;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article title;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article content;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article publishTime;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article updateTime;


}
