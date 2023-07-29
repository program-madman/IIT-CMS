package com.iit.project.cms.CMSServer.dao;
import com.iit.project.cms.CMSServer.entity.BrowsedHistory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class BrowsedHistoryRepository extends JdbcRepository {
    // 添加文章浏览记录
    public boolean addBrowsedHistory(BrowsedHistory browsedHistory) {
        if (getBrowsedHistoryById(browsedHistory.getUserId(), browsedHistory.getArticleId()) != null) {
            // 看过文章了不记录，如果记录，需要删除数据库联合主键
            return true;
        }
        String sql = "INSERT INTO browsed_history (user_id, article_id, time) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, browsedHistory.getUserId(), browsedHistory.getArticleId(), new Date());
        return rowsAffected > 0;
    }

    // 删除文章浏览记录
    public boolean deleteBrowsedHistory(Long userId, Long articleId) {
        String sql = "DELETE FROM browsed_history WHERE user_id = ? AND article_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, articleId);
        return rowsAffected > 0;
    }

    // 根据用户ID和文章ID查询浏览记录
    public BrowsedHistory getBrowsedHistoryById(Long userId, Long articleId) {
        String sql = "SELECT * FROM browsed_history WHERE user_id = ? AND article_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, articleId}, BeanPropertyRowMapper.newInstance(BrowsedHistory.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的浏览记录，返回null
        }
    }
}

