package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.ArticleRepository;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.entity.Article;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.iit.project.cms.CMSServer.common.ExceptionEnum.CREATE_ARTICLE_FAILED;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public BaseResponse getAllArticles(GetAllArticlesRequest request) {
        List<GetAllArticlesResponse> allArticles = articleRepository.getAllArticles(request);
        return BaseResponse.success(allArticles);
    }

    @Override
    public BaseResponse getArticleById(GetArticleDetailRequest request) {
        GetArticleDetailResponse article = articleRepository.getArticleById(request);
        return BaseResponse.success(article);
    }

    @Override
    public BaseResponse createArticle(CreateArticleRequest request) {
        if (articleRepository.createArticle(request)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error(CREATE_ARTICLE_FAILED);
        }
    }

    @Override
    public BaseResponse updateArticle(UpdateArticleRequest request) {
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        return BaseResponse.success(articleRepository.updateArticle(article));
    }

    @Override
    public BaseResponse deleteArticle(DeleteArticleRequest request) {
        return BaseResponse.success(articleRepository.deleteArticle(request.getId()));
    }

    @Override
    public BaseResponse deleteArticleById(Long id) {
        return BaseResponse.success(articleRepository.deleteArticle(id));
    }
}
