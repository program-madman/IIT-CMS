package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.util.List;

@Data
public class SendMsgRequest {
    private Long fromUserId;
    private Long toDeptId;
    private Long toUserId;
    private List<Long> toUserIds;
    private String title;
    private String content;
}
