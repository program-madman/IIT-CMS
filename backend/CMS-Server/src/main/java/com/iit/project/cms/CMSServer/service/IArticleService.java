package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.AddToMyFavRequest;
import com.iit.project.cms.CMSServer.dto.*;

public interface IArticleService {

    BaseResponse getAllArticles(GetAllArticlesRequest request);

    BaseResponse getArticleById(GetArticleDetailRequest request);

    BaseResponse createArticle(CreateArticleRequest request);

    BaseResponse updateArticle(UpdateArticleRequest request);

    BaseResponse deleteArticle(DeleteArticleRequest request);

    BaseResponse deleteArticleById(Long id);

    BaseResponse addComment(AddCommentRequest request);

    BaseResponse likeArticle(LikeArticleRequest request);

    BaseResponse addToMyFav(AddToMyFavRequest request);

    BaseResponse getAllArticleCategories();

    BaseResponse getMyFavArticles(Long uid);
}
