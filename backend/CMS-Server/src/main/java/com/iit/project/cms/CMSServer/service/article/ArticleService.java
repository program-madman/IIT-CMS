package com.iit.project.cms.CMSServer.service.article;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.article.ArticleRepository;
import com.iit.project.cms.CMSServer.dto.article.CreateArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.DeleteArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.GetOneArticleRequest;
import com.iit.project.cms.CMSServer.dto.article.UpdateArticleRequest;
import com.iit.project.cms.CMSServer.entity.article.Article;
import com.iit.project.cms.CMSServer.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public BaseResponse getAllArticles() {
        List<Article> allArticles = articleRepository.getAllArticles();
        return BaseResponse.success(allArticles);
    }

    @Override
    public BaseResponse getArticleById(Long id) {
        Article article = articleRepository.getArticleById(id);
        return BaseResponse.success(article);
    }

    @Override
    public BaseResponse getOneArticle(GetOneArticleRequest request) {
        Article article = articleRepository.getArticleById(request.getId());
        return BaseResponse.success(article);
    }

    @Override
    public BaseResponse createArticle(CreateArticleRequest request) {
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        return BaseResponse.success(articleRepository.createArticle(article));
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
