package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.iit.project.cms.CMSServer.common.Symbol.KEY_HEADER_USER_ID;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @RequestMapping(value = "/getAllArticles")
    public BaseResponse getAllArticles(@RequestHeader(KEY_HEADER_USER_ID) String uid) {
        GetAllArticlesRequest request = new GetAllArticlesRequest();
        request.setUserId(uid);
        return articleService.getAllArticles(request);
    }
    @RequestMapping(value = "/getArticleById")
    public BaseResponse getArticleById(@RequestBody GetArticleDetailRequest request) {
        return articleService.getArticleById(request);
    }

    @RequestMapping(value = "/createArticle")
    public BaseResponse createArticle(@RequestBody CreateArticleRequest request) {
        return articleService.createArticle(request);
    }

    @RequestMapping(value = "/updateArticle")
    public BaseResponse updateArticle(@RequestBody UpdateArticleRequest request) {
        return articleService.updateArticle(request);
    }

    @RequestMapping(value = "/deleteArticle")
    public BaseResponse deleteArticle(@RequestBody DeleteArticleRequest request) {
        return articleService.deleteArticle(request);
    }
}
