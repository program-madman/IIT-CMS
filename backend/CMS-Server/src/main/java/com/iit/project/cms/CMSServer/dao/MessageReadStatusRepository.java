package com.iit.project.cms.CMSServer.dao;
import com.iit.project.cms.CMSServer.entity.MessageReadStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageReadStatusRepository extends JdbcRepository {

    // 添加消息阅读状态
    public boolean createMessageReadStatus(MessageReadStatus messageReadStatus) {
        String sql = "INSERT INTO message_read_status (message_id, user_id, time) " +
                "VALUES (?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, messageReadStatus.getMessage().getMessageId(),
                messageReadStatus.getUser().getUserId(), messageReadStatus.getTime());

        return rowsAffected > 0; // 返回是否插入成功
    }

    // 根据消息ID和用户ID获取消息阅读状态
    public MessageReadStatus getMessageReadStatusById(Long messageId, Long userId) {
        String sql = "SELECT * FROM message_read_status WHERE message_id = ? AND user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{messageId, userId},
                    BeanPropertyRowMapper.newInstance(MessageReadStatus.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的消息阅读状态，返回null
        }
    }

    // 获取所有消息阅读状态
    public List<MessageReadStatus> getAllMessageReadStatus() {
        String sql = "SELECT * FROM message_read_status";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MessageReadStatus.class));
    }

    // 更新消息阅读状态
    public boolean updateMessageReadStatus(MessageReadStatus messageReadStatus) {
        String sql = "UPDATE message_read_status SET time = ? WHERE message_id = ? AND user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, messageReadStatus.getTime(),
                messageReadStatus.getMessage().getMessageId(), messageReadStatus.getUser().getUserId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    // 删除消息阅读状态
    public boolean deleteMessageReadStatus(Long messageId, Long userId) {
        String sql = "DELETE FROM message_read_status WHERE message_id = ? AND user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, messageId, userId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}

