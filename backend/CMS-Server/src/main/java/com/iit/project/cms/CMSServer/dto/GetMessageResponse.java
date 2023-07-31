package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GetMessageResponse {
    private Long messageId;
    private Long fromUser;
    private String fromUserName;
    private Long toUser;
    private String toUserName;
    private String title;
    private String content;
    private Date sendTime;
    private GetUserInfoResponse fromUserInfo;
    private GetUserInfoResponse toUserInfo;
}
