package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class GetAllArticlesResponse {
    private Long articleId;
    private String articleTitle;
    private String articleContent;
    private String publishTime;
    private String authorDeptName;
    private String articleCategory;
    private Integer attachmentTotalCount;
    private Integer likes;
    private Boolean isFav;
    private Boolean isRead;
}
