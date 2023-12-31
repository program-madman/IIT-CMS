package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
import com.iit.project.cms.CMSServer.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping("/article-statistics")
    public BaseResponse getArticleStatisticsInformation() {
        return service.getArticleStatisticsInformation();
    }

    @GetMapping("/author-recentmonth-count-statistics")
    public BaseResponse getAuthorArticleCountInRecentMonth() {
        return service.getAuthorArticleCountInRecentMonth();
    }

    @GetMapping("/queryTopThreeArticleMonth")
    public CommonResult queryTopThreeArticleMonth() {
        return service.queryTopThreeArticleMonth();
    }

    @GetMapping("/queryNoBrowsedUserYear")
    public CommonResult queryNoBrowsedUserYear() {
        return service.queryNoBrowsedUserYear();
    }


}
