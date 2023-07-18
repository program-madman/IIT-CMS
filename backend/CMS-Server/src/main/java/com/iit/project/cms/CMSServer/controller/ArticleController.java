package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.article.CreateArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.DeleteArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.UpdateArticleRequest;
import com.iit.project.cms.CMSServer.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @RequestMapping(value = "/getAllArticles")
    public BaseResponse getAllArticles() {
        return articleService.getAllArticles();
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
