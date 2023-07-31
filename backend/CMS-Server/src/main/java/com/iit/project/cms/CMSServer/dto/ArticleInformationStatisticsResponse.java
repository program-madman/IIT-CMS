package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class ArticleInformationStatisticsResponse {

    private String title;

    private String author;

    private String deptName;

    private Integer commentCount;

    private Integer viewsCount;

    private Integer likeCount;

    private Integer likesRank;

    private Integer favoriteCount;

    private String lastOperation;

    private Date lastOperationTime;

}
