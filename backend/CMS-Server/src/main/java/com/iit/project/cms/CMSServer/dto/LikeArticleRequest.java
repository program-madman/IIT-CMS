package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class LikeArticleRequest {
    private Long userId;
    private Long articleId;
}
