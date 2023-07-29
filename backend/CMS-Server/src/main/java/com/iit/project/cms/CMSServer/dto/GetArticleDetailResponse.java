package com.iit.project.cms.CMSServer.dto;

import com.iit.project.cms.CMSServer.entity.Attachment;
import com.iit.project.cms.CMSServer.entity.Comment;
import lombok.Data;

import java.util.List;

@Data
public class GetArticleDetailResponse {
    private Long articleId;
    private String articleTitle; //文章标题
    private String articleContent; //文章内容
    private String authorName; //发布人名称
    private String publishTime; //发布时间
    private String readCount; //已读人数
    private String replyCount; //回复人数
    private String likeCount; //点赞人数
    private String browseCount; //浏览人数
    private String targetDeptName; //接收部门名称
    private List<ChangeHistoryResponse> changeHistoryResponseList;
    private List<AttachmentResponse> attachmentList; //附件列表
    private List<CommentResponse> commentList; //评论列表
}
