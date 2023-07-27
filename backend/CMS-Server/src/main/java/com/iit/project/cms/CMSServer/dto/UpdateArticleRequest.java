package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UpdateArticleRequest implements Serializable {

    private Long userId;

    private String categoryName;

    private String title;

    private String content;

    private Date publishTime;

    private Date updateTime;

}
