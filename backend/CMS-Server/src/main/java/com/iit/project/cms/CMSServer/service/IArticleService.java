package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.article.CreateArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.DeleteArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.GetOneArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.UpdateArticleRequest;

public interface IArticleService {

    BaseResponse getAllArticles();

    BaseResponse getArticleById(Long id);

    BaseResponse getOneArticle(GetOneArticleRequest request);

    BaseResponse createArticle(CreateArticleRequest request);

    BaseResponse updateArticle(UpdateArticleRequest request);

    BaseResponse deleteArticle(DeleteArticleRequest request);

    BaseResponse deleteArticleById(Long id);
}
