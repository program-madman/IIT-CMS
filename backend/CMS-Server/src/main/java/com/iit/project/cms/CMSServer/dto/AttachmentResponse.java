package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class AttachmentResponse {
    private Long attachmentId;
    private String url;
    private String name;
}
