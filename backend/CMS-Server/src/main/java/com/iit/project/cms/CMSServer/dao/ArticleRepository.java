package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ArticleRepository extends JdbcRepository {
    public boolean createArticle(CreateArticleRequest article) {
        try {
            // 插入文章信息
            String articleSql = "INSERT INTO article (user_id, title, content, category_name) " +
                    "VALUES (?, ?, ?, ?)";
            int r = jdbcTemplate.update(articleSql, article.getUserId(), article.getTitle(),
                    article.getContent(), article.getCategoryName());
            log.info("insert article result : " + r);

            // 插入附件信息
            if (article.getAttachmentList() != null && !article.getAttachmentList().isEmpty()) {
                String attachmentSql = "INSERT INTO attachment (file_name, file_type, size) " +
                        "VALUES (?, ?, ?, ?)";
                for (CreateAttachmentRequest attachment : article.getAttachmentList()) {
                    jdbcTemplate.update(attachmentSql, attachment.getFileName(),
                            attachment.getFileType(), attachment.getSize());

                }
            }

            // 插入发送目标部门信息
            if (article.getTargetDeptList() != null && !article.getTargetDeptList().isEmpty()) {
                String deptSql = "INSERT INTO article_dept (department_name) " +
                        "VALUES (?, ?)";
                for (CreateDeptRequest department : article.getTargetDeptList()) {
                    jdbcTemplate.update(deptSql, department.getDepartmentName());
                }
            }
            return true; // 表示插入成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 表示插入失败
        }
    }

    public GetArticleDetailResponse getArticleById(GetArticleDetailRequest request) {
        Long articleId = request.getArticleId();
        // 查询文章基本信息
        String articleInfoSql = "SELECT\n" +
                "    a.title AS articleTitle,\n" +
                "    a.content AS articleContent,\n" +
                "    CONCAT(u.first_name, ' ', u.last_name) AS authorName,\n" +
                "    DATE_FORMAT(a.publish_time, '%Y-%m-%d %H:%i:%s') AS publishTime,\n" +
                "    (SELECT COUNT(*) FROM article_read_status ars WHERE ars.article_id = a.article_id) AS readCount,\n" +
                "    (SELECT COUNT(*) FROM comments cmt WHERE cmt.article_id = a.article_id) AS replyCount,\n" +
                "    (SELECT COUNT(*) FROM article_like al WHERE al.article_id = a.article_id) AS likeCount,\n" +
                "    (SELECT COUNT(*) FROM browsed_history bh WHERE bh.article_id = a.article_id) AS browseCount,\n" +
                "    (SELECT GROUP_CONCAT(DISTINCT d.dept_name SEPARATOR ', ') FROM audience aud INNER JOIN department d ON aud.dept_id = d.dept_id WHERE aud.article_id = a.article_id) AS targetDeptName\n" +
                "FROM\n" +
                "    article a\n" +
                "LEFT JOIN\n" +
                "    user u ON a.user_id = u.user_id\n" +
                "WHERE\n" +
                "    a.article_id = ?;";
        GetArticleDetailResponse response = jdbcTemplate.queryForObject(
                articleInfoSql, new Object[]{articleId}, BeanPropertyRowMapper.newInstance(GetArticleDetailResponse.class)
        );
        return response;
    }

    public List<GetAllArticlesResponse> getAllArticles(GetAllArticlesRequest request) {
        List<GetAllArticlesResponse> list = new ArrayList<>();
        String userId = request.getUserId();

        String sql = "SELECT * FROM article WHERE article_id IN \n" +
                "(SELECT article_id FROM cms.audience\n" +
                "WHERE dept_id = (SELECT dept_id FROM user WHERE user_id = ?));";

        List<Article> articles = jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Article.class));
        for (Article article : articles) {
            GetAllArticlesResponse response = new GetAllArticlesResponse();
            response.setArticleId(article.getArticleId());
            response.setArticleTitle(article.getTitle());
            response.setArticleContent(article.getContent());
            response.setPublishTime(String.valueOf(article.getPublishTime()));
            response.setArticleCategory(article.getCategoryName());
            response.setAttachmentTotalCount(countAttachmentsByArticleId(article.getArticleId()));
            response.setLikes(countLikesByArticleId(article.getArticleId()));
            response.setIsRead(isArticleRead(article.getArticleId(), Long.parseLong(userId)));
            response.setIsFav(isArticleFavorite(article.getArticleId(), Long.parseLong(userId)));
            list.add(response);
        }
        return list;
    }

    private int countAttachmentsByArticleId(long articleId) {
        String sql = "SELECT COUNT(*) AS attachment_count FROM attachment WHERE article_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{articleId}, Integer.class);
    }

    private int countLikesByArticleId(long articleId) {
        String sql = "SELECT COUNT(user_id) AS like_count FROM article_like WHERE article_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{articleId}, Integer.class);
    }

    private boolean isArticleRead(long articleId, long userId) {
        String sql = "SELECT COUNT(*) FROM article_read_status WHERE article_id = ? AND user_id = ? AND is_read = 1";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{articleId, userId}, Integer.class);
        return count > 0;
    }

    private boolean isArticleFavorite(long articleId, long userId) {
        String sql = "SELECT COUNT(*) > 0 AS is_favorite FROM favorite WHERE user_id = ? AND article_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, articleId}, Boolean.class);
    }

    public List<GetMyArticlesResponse> getMyPublishedArticles(GetMyArticlesRequest request) {

        return null;
    }

    public boolean updateArticle(Article article) {
        String sql = "UPDATE article SET user_id = ?, category_name = ?, title = ?, content = ?, publish_time = ?, " +
                "update_time = ? WHERE article_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, article.getUserId(), article.getCategoryName(),
                article.getTitle(), article.getContent(), article.getPublishTime(), article.getUpdateTime(),
                article.getArticleId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteArticle(Long articleId) {
        String sql = "DELETE FROM article WHERE article_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, articleId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}
