package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService implements IQueryService {
    @Autowired
    private QueryRepository queryRepository;


    @Override
    public BaseResponse getUsers() {
        return BaseResponse.success(queryRepository.getUsers());
    }

    @Override
    public BaseResponse getUserBrowsedHistory() {
        return BaseResponse.success(queryRepository.getUserBrowsedHistory());
    }

    @Override
    public BaseResponse getUserComments() {
        return BaseResponse.success(queryRepository.getUserComments());
    }

    @Override
    public BaseResponse getMessages() {
        return BaseResponse.success(queryRepository.getMessages());
    }

    @Override
    public BaseResponse getOperateHistory() {
        return BaseResponse.success(queryRepository.getOperateHistory());
    }

    @Override
    public BaseResponse getTotalUserStatistics() {
        return BaseResponse.success(queryRepository.getTotalUserStatistics());
    }

    @Override
    public BaseResponse getArticleReadStatusDetails() {
        return BaseResponse.success(queryRepository.getArticleReadStatusDetails());
    }

    @Override
    public BaseResponse getAttachmentDetails() {
        return BaseResponse.success(queryRepository.getAttachmentDetails());
    }

    @Override
    public BaseResponse getLikeDetails() {
        return BaseResponse.success(queryRepository.getLikeDetails());
    }

    @Override
    public BaseResponse getArticleStatistics() {
        return BaseResponse.success(queryRepository.getArticleStatistics());
    }
}
