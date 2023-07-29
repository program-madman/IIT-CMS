package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class CreateAttachmentRequest {
    private String fileName;
    private String fileType;
    private long size;
}
