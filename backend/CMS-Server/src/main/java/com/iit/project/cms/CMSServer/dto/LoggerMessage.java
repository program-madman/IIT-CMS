package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class LoggerMessage {
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
}
