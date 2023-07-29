package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CreateArticleRequest implements Serializable {

    private Long userId;//创建者id

    private String title;//文章标题

    private String content;//文章内容

    private String categoryName;//文章类别

    private List<CreateAttachmentRequest> attachmentList;//附件列表

    private List<CreateDeptRequest> targetDeptList;//发送目标部门，支持多个



}
