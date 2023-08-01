package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;

public interface IStatisticsService {

    BaseResponse getArticleStatisticsInformation();

    BaseResponse getAuthorArticleCountInRecentMonth();

    CommonResult queryTopThreeArticleMonth();

    CommonResult queryNoBrowsedUserYear();

}
