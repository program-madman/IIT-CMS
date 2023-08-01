package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;

public interface IQueryService {
    BaseResponse getUsers();

    BaseResponse getUserBrowsedHistory();

    BaseResponse getUserComments();

    BaseResponse getMessages();

    BaseResponse getOperateHistory();

    BaseResponse getTotalUserStatistics();

    BaseResponse getArticleReadStatusDetails();

    BaseResponse getAttachmentDetails();

    BaseResponse getLikeDetails();

    BaseResponse getArticleStatistics();
}
