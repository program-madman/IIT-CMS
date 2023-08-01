package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class AuthorArticleCountInRecentMonthResponse {

    public String userId;
    
    public Integer previousMonthArticles;

    public Integer currentMonthArticles;

    public Integer difference;

}
