package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class GetMyArticlesResponse {
    private String articleTitle;
    private String articleContent;
    private String publishTime;
    private String authorDeptName;
    private String attachmentTotalCount;
    private String articleCategory;
    private String likes;
    private Boolean isFav;
    private Boolean isRead;

}
