package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private String commentId;
    private String commentViewerName;//评论人姓名
    private String commentViewerDept;//评论人部门
    private String commentViewerType;//评论人类型
    private String commentTime;//评论时间
}
