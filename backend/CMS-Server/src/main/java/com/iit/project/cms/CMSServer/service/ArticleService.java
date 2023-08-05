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
    private AttachmentRepository attachmentRepository;

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

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private OperateHistoryRepository operateHistoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BaseResponse getAllArticles(GetAllArticlesRequest request) {
        List<GetAllArticlesResponse> list = new ArrayList<>();
        List<Article> allArticles = articleRepository.getAllArticles(request);
        for (Article article : allArticles) {
            GetAllArticlesResponse response = new GetAllArticlesResponse();
            response.setArticleId(article.getArticleId());
            response.setArticleTitle(article.getTitle());
            response.setArticleContent(article.getContent());
            response.setAuthorDeptName(departmentRepository.getDepartmentNameByUserId(Long.parseLong(request.getUserId())));
            response.setPublishTime(String.valueOf(article.getPublishTime()));
            response.setArticleCategory(article.getCategoryName());
            response.setAttachmentTotalCount(articleRepository.countAttachmentsByArticleId(article.getArticleId()));
            response.setIsLike(articleRepository.isArticleLike(article.getArticleId(), Long.parseLong(request.getUserId())));
            response.setLikes(articleRepository.countLikesByArticleId(article.getArticleId()));
            response.setIsRead(articleRepository.isArticleRead(article.getArticleId(), Long.parseLong(request.getUserId())));
            response.setIsFav(articleRepository.isArticleFavorite(article.getArticleId(), Long.parseLong(request.getUserId())));
            list.add(response);
        }
        return BaseResponse.success(list);
    }

    @Override
    public BaseResponse getAllArticlesOneSql(GetAllArticlesRequest request) {
        return BaseResponse.success(articleRepository.getAllArticlesOneSql(request));
    }

    @Override
    public BaseResponse getAllArticlesPublishedByMe(GetAllArticlesRequest request) {
        List<GetAllArticlesResponse> list = new ArrayList<>();
        List<Article> allArticles = articleRepository.getAllArticlesPublishedByMe(request);

        for (Article article : allArticles) {
            GetAllArticlesResponse response = new GetAllArticlesResponse();
            response.setArticleId(article.getArticleId());
            response.setArticleTitle(article.getTitle());
            response.setArticleContent(article.getContent());
            response.setAuthorDeptName(departmentRepository.getDepartmentNameByUserId(Long.parseLong(request.getUserId())));
            response.setPublishTime(String.valueOf(article.getPublishTime()));
            response.setArticleCategory(article.getCategoryName());
            response.setAttachmentTotalCount(articleRepository.countAttachmentsByArticleId(article.getArticleId()));
            response.setLikes(articleRepository.countLikesByArticleId(article.getArticleId()));
            response.setIsLike(articleRepository.isArticleLike(article.getArticleId(), Long.parseLong(request.getUserId())));
            response.setIsRead(articleRepository.isArticleRead(article.getArticleId(), Long.parseLong(request.getUserId())));
            response.setIsFav(articleRepository.isArticleFavorite(article.getArticleId(), Long.parseLong(request.getUserId())));
            list.add(response);
        }
        return BaseResponse.success(list);
    }

    @Override
    public BaseResponse getAllArticlesPublishedByMeOneSql(GetAllArticlesRequest request) {
        return BaseResponse.success(articleRepository.getAllArticlesPublishedByMeOneSql(request));
    }

    @Override
    public BaseResponse getArticleById(GetArticleDetailRequest request) {
        GetArticleDetailResponse article = articleRepository.getArticleById(request);
        article.setCommentList(getCommentListByArticleId(article.getArticleId()));
        article.setAttachmentList(getAttachmentListByArticleId(article.getArticleId()));
        article.setChangeHistoryResponseList(getChangeHistoryByArticleId(article.getArticleId()));
        updateReadStatus(request.getUserId(), request.getArticleId());
        updateBrowseHistory(request.getUserId(), request.getArticleId());
        return BaseResponse.success(article);
    }

    @Override
    public BaseResponse getArticleByIdOneSql(GetArticleDetailRequest request) {
        return null;
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
            return BaseResponse.error("Like or Unlike failed!");
        }
    }

    @Override
    public BaseResponse addToMyFav(AddToMyFavRequest request) {
        Favorite favorite = new Favorite();
        favorite.setUserId(request.getUserId());
        favorite.setArticleId(request.getArticleId());
        if (favoriteRepository.addToFavorites(favorite)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Add/Delete article to/from My Favorites failed!");
        }
    }

    @Override
    public BaseResponse getAllArticleCategories() {
        List<Category> allCategories = categoryRepository.getAllCategories();
        List<GetAllCategoriesResponse> responseList = new ArrayList<>();
        for (Category category : allCategories) {
            GetAllCategoriesResponse response = new GetAllCategoriesResponse();
            BeanUtils.copyProperties(category, response);
            responseList.add(response);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse getMyFavArticles(Long uid) {
        List<Favorite> favoriteArticles = favoriteRepository.getAllFavoritesByUserId(uid);
        List<Long> ids = new ArrayList<>();
        for (Favorite f : favoriteArticles) {
            ids.add(f.getArticleId());
        }
        List<Article> articles = articleRepository.getArticlesByIds(ids);
        List<GetFavArticlesResponse> responseList = new ArrayList<>();
        for (Article article : articles) {
            GetFavArticlesResponse response = new GetFavArticlesResponse();
            response.setArticleId(article.getArticleId());
            response.setArticleTitle(article.getTitle());
            response.setArticleContent(article.getContent());
            response.setAuthorDeptName(departmentRepository.getDepartmentNameByUserId(uid));
            response.setPublishTime(String.valueOf(article.getPublishTime()));
            response.setArticleCategory(article.getCategoryName());
            response.setAttachmentTotalCount(articleRepository.countAttachmentsByArticleId(article.getArticleId()));
            response.setIsLike(articleRepository.isArticleLike(article.getArticleId(), uid));
            response.setLikes(articleRepository.countLikesByArticleId(article.getArticleId()));
            response.setIsRead(articleRepository.isArticleRead(article.getArticleId(), uid));
            response.setIsFav(articleRepository.isArticleFavorite(article.getArticleId(), uid));
            responseList.add(response);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse getMyFavArticlesOneSql(Long uid) {
        List<GetFavArticlesResponse> favoriteArticles = favoriteRepository.getMyFavArticlesOneSql(uid);
        return BaseResponse.success(favoriteArticles);
    }



    private List<CommentResponse> getCommentListByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.getCommentsByArticleId(articleId);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : comments) {
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
            r.setCommentContent(comment.getContent());
            commentResponseList.add(r);
        }
        return commentResponseList;
    }

    private List<AttachmentResponse> getAttachmentListByArticleId(Long articleId) {
        List<Attachment> attachments = attachmentRepository.getAttachmentsByArticleId(articleId);
        List<AttachmentResponse> result = new ArrayList<>();
        for (Attachment attachment : attachments) {
            AttachmentResponse response = new AttachmentResponse();
            response.setAttachmentId(attachment.getAttachmentId());
            response.setName(attachment.getFileName());
            Media media = mediaRepository.getMediaByAttachmentId(attachment.getAttachmentId());
            response.setUrl(media.getUrl());
            result.add(response);
        }
        return result;
    }

    private List<ChangeHistoryResponse> getChangeHistoryByArticleId(Long articleId) {
        List<OperateHistory> operateHistoryList = operateHistoryRepository.getOperateHistoriesByArticleId(articleId);
        List<ChangeHistoryResponse> result = new ArrayList<>();
        for (OperateHistory operateHistory : operateHistoryList) {
            ChangeHistoryResponse response = new ChangeHistoryResponse();
            response.setArticleId(articleId);
            response.setOperationType(operateHistory.getOperateType());
            response.setUserId(operateHistory.getUserId());
            response.setOperationTime(operateHistory.getTime());
            User user = userRepository.getUserById(operateHistory.getUserId());
            response.setOperatorName(user.getFirstName() + " " + user.getLastName());
            result.add(response);
        }
        return result;
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
