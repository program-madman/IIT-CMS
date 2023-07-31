package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService implements IStatisticsService {

    @Autowired
    private StatisticsRepository repository;

    @Override
    public BaseResponse getArticleStatisticsInformation() {
        return BaseResponse.success(repository.getArticleInformation());
    }
}
