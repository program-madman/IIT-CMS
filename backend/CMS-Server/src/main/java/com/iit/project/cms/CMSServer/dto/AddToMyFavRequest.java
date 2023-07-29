package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class AddToMyFavRequest {
    private Long userId;
    private Long articleId;
}
