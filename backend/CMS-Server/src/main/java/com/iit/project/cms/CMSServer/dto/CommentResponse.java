package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private Long commentId;
    private String commentContent;
    private String commenterName;//评论人姓名
    private String commenterDept;//评论人部门
    private String commenterType;//评论人类型
    private Date commentTime;//评论时间
}
