package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.*;

public interface IArticleService {

    BaseResponse getAllArticles(GetAllArticlesRequest request);

    BaseResponse getArticleById(GetArticleDetailRequest request);

    BaseResponse createArticle(CreateArticleRequest request);

    BaseResponse updateArticle(UpdateArticleRequest request);

    BaseResponse deleteArticle(DeleteArticleRequest request);

    BaseResponse deleteArticleById(Long id);

    BaseResponse addComment(AddCommentRequest request);
}
