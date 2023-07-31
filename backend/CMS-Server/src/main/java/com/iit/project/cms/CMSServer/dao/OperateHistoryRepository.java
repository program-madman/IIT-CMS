package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.OperateHistory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperateHistoryRepository extends JdbcRepository {
    public boolean createOperateHistory(OperateHistory operateHistory) {
        String sql = "INSERT INTO operate_history (user_id, article_id, operate_type, time) " +
                "VALUES (?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, operateHistory.getUserId(), operateHistory.getArticleId(),
                operateHistory.getOperateType(), operateHistory.getTime());

        return rowsAffected > 0; // Return whether the insertion was successful
    }

    public List<OperateHistory> getOperateHistoriesByUserId(Long userId) {
        String sql = "SELECT * FROM operate_history WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(OperateHistory.class));
    }

    public List<OperateHistory> getOperateHistoriesByArticleId(Long articleId) {
        String sql = "SELECT * FROM operate_history WHERE article_id = ?";
        return jdbcTemplate.query(sql, new Object[]{articleId}, BeanPropertyRowMapper.newInstance(OperateHistory.class));
    }

    public boolean deleteOperateHistory(Long operateId) {
        String sql = "DELETE FROM operate_history WHERE operate_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, operateId);

        return rowsAffected > 0; // Return whether the deletion was successful
    }
}
