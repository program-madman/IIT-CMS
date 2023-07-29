package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.*;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.iit.project.cms.CMSServer.common.ExceptionEnum.CREATE_ARTICLE_FAILED;
import static com.iit.project.cms.CMSServer.common.ExceptionEnum.CREATE_COMMENT_FAILED;

@Slf4j
@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private ArticleReadStatusRepository articleReadStatusRepository;

    @Autowired
    private BrowsedHistoryRepository browsedHistoryRepository;

    @Override
    public BaseResponse getAllArticles(GetAllArticlesRequest request) {
        List<GetAllArticlesResponse> allArticles = articleRepository.getAllArticles(request);
        return BaseResponse.success(allArticles);
    }

    @Override
    public BaseResponse getArticleById(GetArticleDetailRequest request) {
        GetArticleDetailResponse article = articleRepository.getArticleById(request);
        article.setCommentList(getCommentListByArticleId(article.getArticleId()));
        // TODO: 2023/7/30 附件
        updateReadStatus(request.getUserId(), request.getArticleId());
        updateBrowseHistory(request.getUserId(), request.getArticleId());
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

    @Override
    public BaseResponse addComment(AddCommentRequest request) {
        Comment comment = new Comment();
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());
        comment.setArticleId(request.getArticleId());
        if (commentRepository.createComment(comment)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error(CREATE_COMMENT_FAILED);
        }
    }

    @Override
    public BaseResponse likeArticle(LikeArticleRequest request) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticleId(request.getArticleId());
        articleLike.setUserId(request.getUserId());
        if (articleLikeRepository.addArticleLike(articleLike)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Like failed");
        }
    }


    private List<CommentResponse> getCommentListByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.getCommentsByArticleId(articleId);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : comments) {
            log.info(comment.toString());
            CommentResponse r = new CommentResponse();
            r.setCommentId(comment.getCommentId());
            r.setCommentTime(comment.getPublishTime());
            Long userId = comment.getUserId();
            User user = userRepository.getUserById(userId);
            Long deptId = user.getDeptId();
            Department dept = departmentRepository.getDepartmentById(deptId);
            r.setCommenterDept(dept.getDeptName());
            String fullName = user.getFirstName() + " " + user.getLastName();
            r.setCommenterName(fullName);
            r.setCommenterType(user.getUserType());
            commentResponseList.add(r);
        }
        return commentResponseList;
    }


    private void updateReadStatus(Long userId, Long articleId) {
        ArticleReadStatus articleReadStatus = new ArticleReadStatus();
        articleReadStatus.setUserId(userId);
        articleReadStatus.setArticleId(articleId);
        articleReadStatusRepository.addArticleReadStatus(articleReadStatus);
    }


    private void updateBrowseHistory(Long userId, Long articleId) {
        BrowsedHistory browsedHistory = new BrowsedHistory();
        browsedHistory.setUserId(userId);
        browsedHistory.setArticleId(articleId);
        browsedHistoryRepository.addBrowsedHistory(browsedHistory);
    }
}
