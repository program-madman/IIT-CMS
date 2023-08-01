package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class AuthorArticleCountInRecentMonthResponse {

    public Long userId;

    private String name;

    private String deptName;
    
    public Integer previousMonthArticles;

    public Integer currentMonthArticles;

    public Integer difference;

}
