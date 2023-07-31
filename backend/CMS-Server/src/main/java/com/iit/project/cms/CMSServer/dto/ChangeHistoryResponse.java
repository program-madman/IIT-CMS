package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChangeHistoryResponse {
    private Long userId;
    private Long articleId;
    private String operatorName;
    private String operationType;
    private Date operationTime;
}
