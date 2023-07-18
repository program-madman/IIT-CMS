package com.iit.project.cms.CMSServer.dto.article;

import lombok.Data;

import java.io.Serializable;
@Data
public class GetOneArticleRequest implements Serializable {
    private Long id;
}
