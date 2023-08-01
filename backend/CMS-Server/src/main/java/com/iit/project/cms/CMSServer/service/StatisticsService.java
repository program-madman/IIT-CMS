package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
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

    @Override
    public BaseResponse getAuthorArticleCountInRecentMonth() {
        return BaseResponse.success(repository.getAuthorArticleCountInRecentMonth());
    }

    @Override
    public CommonResult queryTopThreeArticleMonth() {

        CommonResult result = new CommonResult();

        try {
            result.setResult(repository.queryTopThreeArticleMonth());
            result.setSuccess(true);

        }catch(Exception e){
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }


    @Override
    public CommonResult queryNoBrowsedUserYear() {

        CommonResult result = new CommonResult();

        try {
            result.setResult(repository.queryNoBrowsedUserYear());
            result.setSuccess(true);

        }catch(Exception e){
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

}
