package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class GetArticleDetailRequest implements Serializable {
    private Long userId;
    private Long articleId;
}
