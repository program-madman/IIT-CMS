package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            // Get the generated article_id
            Long articleId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

            // 插入发送目标部门信息
            if (article.getTargetDeptList() != null && !article.getTargetDeptList().isEmpty()) {
                String audienceSql = "INSERT INTO audience (dept_id, article_id) VALUES (?, ?)";
                for (CreateDeptRequest department : article.getTargetDeptList()) {
                    // Associate the article with the department in the audience table
                    jdbcTemplate.update(audienceSql, department.getDepartmentId(), articleId);
                }
            }

            // 插入附件信息
            if (article.getAttachmentList() != null && !article.getAttachmentList().isEmpty()) {
                String attachmentSql = "INSERT INTO attachment (file_name, file_type, size) " +
                        "VALUES (?, ?, ?, ?)";
                for (CreateAttachmentRequest attachment : article.getAttachmentList()) {
                    jdbcTemplate.update(attachmentSql, attachment.getFileName(),
                            attachment.getFileType(), attachment.getSize());

                }
            }
            return true; // 表示插入成功
        } catch (Exception e) {
            e.printStackTrace();
            BaseResponse.error("Insert article or dept failed.");
            return false; // 表示插入失败
        }
    }

    public GetArticleDetailResponse getArticleById(GetArticleDetailRequest request) {
        Long articleId = request.getArticleId();
        // 查询文章基本信息
        String articleInfoSql = "SELECT\n" +
                "    a.article_id AS articleId,\n" +
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

    public List<Article> getAllArticles(GetAllArticlesRequest request) {
        String userId = request.getUserId();
        String sql = "SELECT * FROM article WHERE article_id IN \n" +
                "(SELECT article_id FROM cms.audience\n" +
                "WHERE dept_id = (SELECT dept_id FROM user WHERE user_id = ?));";

        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Article.class));
    }


    public List<Article> getAllArticlesPublishedByMe(GetAllArticlesRequest request) {
        String userId = request.getUserId();
        String sql = "SELECT * FROM article WHERE user_id = ?;";

        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Article.class));
    }

    public List<Article> getArticlesByIds(List<Long> articleIds) {
        String sql = "SELECT * FROM article WHERE article_id IN (" +
                articleIds.stream().map(String::valueOf).collect(Collectors.joining(",")) +
                ")";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Article.class));
    }

    public int countAttachmentsByArticleId(long articleId) {
        String sql = "SELECT COUNT(*) AS attachment_count FROM attachment WHERE article_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{articleId}, Integer.class);
    }

    public int countLikesByArticleId(long articleId) {
        String sql = "SELECT COUNT(user_id) AS like_count FROM article_like WHERE article_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{articleId}, Integer.class);
    }

    public boolean isArticleRead(long articleId, long userId) {
        String sql = "SELECT COUNT(*) FROM article_read_status WHERE article_id = ? AND user_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{articleId, userId}, Integer.class);
        return count > 0;
    }

    public boolean isArticleFavorite(long articleId, long userId) {
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
