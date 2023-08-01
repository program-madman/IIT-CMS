package com.iit.project.cms.CMSServer.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class QueryRepository extends JdbcRepository {

    public List<Map<String, Object>> getUsers() {
        String sql = "SELECT u.user_name, u.user_type, dept_tb.dept_name, role_tb.role_name, art_tb.title " +
                "FROM user u " +
                "LEFT JOIN (SELECT dept_id, dept_name FROM department) AS dept_tb ON u.dept_id = dept_tb.dept_id " +
                "LEFT JOIN (SELECT role_id, role_name FROM role) AS role_tb ON u.role_id = role_tb.role_id " +
                "LEFT JOIN (SELECT * FROM favorite) AS favor_tb ON u.user_id = favor_tb.user_id " +
                "LEFT JOIN (SELECT article_id, title FROM article) AS art_tb ON u.user_id = art_tb.article_id";

        return jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> getUserBrowsedHistory() {
        String sql = "SELECT user_tb.user_name, bh.time as browsed_time, article.category_name, " +
                "article.title, article.content, article.publish_time, article.update_time " +
                "FROM browsed_history bh " +
                "INNER JOIN (SELECT user_id, user_name FROM user) AS user_tb " +
                "ON bh.user_id = user_tb.user_id " +
                "INNER JOIN (SELECT * FROM article) AS article " +
                "ON bh.article_id = article.article_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getUserComments() {
        String sql = "SELECT user_tb.user_name, comments.content as comment_content, " +
                "comments.publish_time as comment_time, article_tb.category_name, " +
                "article_tb.title " +
                "FROM comments " +
                "INNER JOIN (SELECT user_id, user_name FROM user) AS user_tb " +
                "ON comments.user_id = user_tb.user_id " +
                "INNER JOIN (SELECT article_id, category_name, title, content FROM article) AS article_tb " +
                "ON comments.article_id = article_tb.article_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getMessages() {
        String sql = "SELECT user_tb.user_name AS from_user, user_tb_se.user_name AS to_user, " +
                "message.title, message.content, message.send_time " +
                "FROM message " +
                "LEFT JOIN (SELECT user_id, user_name FROM user) AS user_tb " +
                "ON message.from_user = user_tb.user_id " +
                "LEFT JOIN (SELECT user_id, user_name FROM user) AS user_tb_se " +
                "ON message.to_user = user_tb_se.user_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getOperateHistory() {
        String sql = "SELECT user_tb.user_name, art_tb.title, operate_history.operate_type, " +
                "operate_history.time AS operate_time " +
                "FROM operate_history " +
                "LEFT JOIN (SELECT article_id, title FROM article) AS art_tb " +
                "ON operate_history.article_id = art_tb.article_id " +
                "LEFT JOIN (SELECT user_id, user_name FROM user) AS user_tb " +
                "ON operate_history.user_id = user_tb.user_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getTotalUserStatistics() {
        String sql = "SELECT 'total_user' AS user_type, " +
                "SUM(sign_in_count) AS total_sign_in_count, " +
                "SUM(publish_aritcle_total_count) AS total_publish_aritcle_total_count " +
                "FROM statistics_user " +
                "UNION ALL " +
                "SELECT user_id, sign_in_count, publish_aritcle_total_count " +
                "FROM statistics_user";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getArticleReadStatusDetails() {
        String sql = "SELECT art_tb.article_id AS article_id, " +
                "title, " +
                "user_tb.user_id AS user_id, " +
                "user_name, " +
                "read_time " +
                "FROM article_read_status " +
                "INNER JOIN (SELECT user_id, user_name FROM user) AS user_tb " +
                "ON article_read_status.user_id = user_tb.user_id " +
                "INNER JOIN (SELECT article_id, user_id, category_name, title, content FROM article) AS art_tb " +
                "ON article_read_status.article_id = art_tb.article_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getAttachmentDetails() {
        String sql = "SELECT " +
                "file_name, " +
                "file_type, " +
                "size, " +
                "attach_time, " +
                "title, " +
                "save_name, " +
                "url " +
                "FROM attachment " +
                "LEFT JOIN (SELECT article_id, title FROM article) AS art_tb ON attachment.article_id = art_tb.article_id " +
                "LEFT JOIN (SELECT attachment_id, save_name, url FROM media) AS media_tb ON attachment.attachment_id = media_tb.attachment_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getLikeDetails() {
        String sql = "SELECT " +
                "title, " +
                "user_name, " +
                "time AS like_time " +
                "FROM article_like " +
                "LEFT JOIN (SELECT article_id, title FROM article) AS art_tb ON article_like.article_id = art_tb.article_id " +
                "LEFT JOIN (SELECT user_id, user_name FROM user) AS user_tb ON article_like.user_id = user_tb.user_id";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getArticleStatistics() {
        String sql = "SELECT " +
                "'total_article' AS total_article, " +
                "SUM(like_count) AS article_total_like, " +
                "SUM(favorite_count) AS article_total_favorite, " +
                "SUM(browsed_count) AS article_total_count, " +
                "SUM(comment_count) AS article_total_comment " +
                "FROM statistics_article " +
                "UNION ALL " +
                "SELECT " +
                "article_id, " +
                "like_count, " +
                "favorite_count, " +
                "browsed_count, " +
                "comment_count " +
                "FROM statistics_article";

        return jdbcTemplate.queryForList(sql);
    }
}
