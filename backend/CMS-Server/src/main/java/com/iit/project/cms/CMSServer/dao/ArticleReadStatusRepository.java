package com.iit.project.cms.CMSServer.dao;
import com.iit.project.cms.CMSServer.entity.ArticleReadStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ArticleReadStatusRepository extends JdbcRepository {
    // 添加文章阅读记录
    public boolean addArticleReadStatus(ArticleReadStatus articleReadStatus) {
        if (getArticleReadStatusById(articleReadStatus.getUserId(), articleReadStatus.getArticleId()) != null) {
            // 阅读过不再更新记录
            return true;
        }
        String sql = "INSERT INTO article_read_status (user_id, article_id, read_time) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, articleReadStatus.getUserId(), articleReadStatus.getArticleId(), new Date());
        return rowsAffected > 0;
    }

    // 删除文章阅读记录
    public boolean deleteArticleReadStatus(Long userId, Long articleId) {
        String sql = "DELETE FROM article_read_status WHERE user_id = ? AND article_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, articleId);
        return rowsAffected > 0;
    }

    // 根据用户ID和文章ID查询阅读记录
    public ArticleReadStatus getArticleReadStatusById(Long userId, Long articleId) {
        String sql = "SELECT * FROM article_read_status WHERE user_id = ? AND article_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, articleId}, BeanPropertyRowMapper.newInstance(ArticleReadStatus.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的阅读记录，返回null
        }
    }

}

