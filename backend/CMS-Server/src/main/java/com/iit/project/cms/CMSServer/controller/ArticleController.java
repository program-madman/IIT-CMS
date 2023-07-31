package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
import com.iit.project.cms.CMSServer.dto.AddToMyFavRequest;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.service.IArticleService;
import com.iit.project.cms.CMSServer.service.IAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.iit.project.cms.CMSServer.common.Symbol.KEY_HEADER_USER_ID;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IAttachmentService attachmentService;

    @RequestMapping(value = "/getAllArticles")
    public BaseResponse getAllArticles(@RequestHeader(KEY_HEADER_USER_ID) String uid) {
        GetAllArticlesRequest request = new GetAllArticlesRequest();
        request.setUserId(uid);
        return articleService.getAllArticles(request);
    }
    @RequestMapping(value = "/getArticleById")
    public BaseResponse getArticleById(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody GetArticleDetailRequest request) {
        request.setUserId(Long.parseLong(uid));
        return articleService.getArticleById(request);
    }

    @RequestMapping(value = "/createArticle")
    public BaseResponse createArticle(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody CreateArticleRequest request) {
        request.setUserId(Long.parseLong(uid));
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

    @RequestMapping(value = "/addComment")
    public BaseResponse addComment(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody AddCommentRequest request) {
        request.setUserId(Long.parseLong(uid));
        return articleService.addComment(request);
    }

    @RequestMapping(value = "/uploadAttachments")
    public BaseResponse uploadAttachments(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestParam("file") MultipartFile file) {
        return attachmentService.upload(uid, file);
    }
    @RequestMapping(value = "/downloadAttachment")
    public BaseResponse downloadAttachment(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestParam("file") MultipartFile file) {
        return attachmentService.upload(uid, file);
    }

    @RequestMapping(value = "/likeArticle")
    public BaseResponse likeArticle(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody LikeArticleRequest request) {
        request.setUserId(Long.parseLong(uid));
        return articleService.likeArticle(request);
    }

    @RequestMapping(value = "/addToMyFav")
    public BaseResponse addToMyFav(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody AddToMyFavRequest request) {
        request.setUserId(Long.parseLong(uid));
        return articleService.addToMyFav(request);
    }
    @RequestMapping(value = "/getMyFavArticles")
    public BaseResponse getMyFavArticles(@RequestHeader(KEY_HEADER_USER_ID) Long uid) {
        return articleService.getMyFavArticles(uid);
    }

    @RequestMapping(value = "/getAllArticleCategories")
    public BaseResponse getAllArticleCategories() {
        return articleService.getAllArticleCategories();
    }


}
