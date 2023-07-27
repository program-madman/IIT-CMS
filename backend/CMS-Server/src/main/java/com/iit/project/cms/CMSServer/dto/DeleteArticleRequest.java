package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class DeleteArticleRequest implements Serializable {
    private Long id;
}
