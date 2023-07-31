package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.Message;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class MessageRepository extends JdbcRepository {

    // 添加消息
    public boolean createMessage(Message message) {
        String sql = "INSERT INTO message (from_user, to_user, title, content, send_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, message.getFromUser(), message.getToUser(),
                message.getTitle(), message.getContent(), new Date());

        return rowsAffected > 0; // 返回是否插入成功
    }

    // 根据消息ID获取消息
    public Message getMessageById(Long messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{messageId}, BeanPropertyRowMapper.newInstance(Message.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的消息，返回null
        }
    }

    // 获取所有消息
    public List<Message> getAllMessages() {
        String sql = "SELECT * FROM message";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Message.class));
    }

    // 发送给用户的消息
    public List<Message> getMessagesSentToUser(Long userId) {
        String sql = "SELECT * FROM message WHERE to_user = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Message.class));
    }

    // 用户发送的消息
    public List<Message> getMessagesSentByUser(Long userId) {
        String sql = "SELECT * FROM message WHERE from_user = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Message.class));
    }

    // 更新消息
    public boolean updateMessage(Message message) {
        String sql = "UPDATE message SET from_user = ?, to_user = ?, title = ?, content = ?, send_time = ? " +
                "WHERE message_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, message.getFromUser(), message.getToUser(),
                message.getTitle(), message.getContent(), new Date(), message.getMessageId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    // 删除消息
    public boolean deleteMessage(Long messageId) {
        String sql = "DELETE FROM message WHERE message_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, messageId);

        return rowsAffected > 0; // 返回是否删除成功
    }

    // 批量插入消息
    public boolean batchInsertMessages(List<Message> messages) {
        boolean ret = false;
        for (Message message : messages) {
             ret =createMessage(message);
        }
//        String sql = "INSERT INTO message (message_id, from_user, to_user, title, content, send_time) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        int batchSize = 1000; // 每次批量插入的大小，可以根据实际情况调整
//        int totalSize = messages.size();
//        int batches = (totalSize + batchSize - 1) / batchSize; // 计算批次数
//
//        int[] affectedRows = new int[totalSize];
//
//        for (int i = 0; i < batches; i++) {
//            List<Message> batchList = messages.subList(i * batchSize, Math.min((i + 1) * batchSize, totalSize));
//            int[] result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int j) throws SQLException {
//                    Message message = batchList.get(j);
//                    ps.setLong(1, message.getMessageId());
//                    ps.setLong(2, message.getFromUser());
//                    ps.setLong(3, message.getToUser());
//                    ps.setString(4, message.getTitle());
//                    ps.setString(5, message.getContent());
//                    ps.setTimestamp(6, new Timestamp(new Date().getTime()));
//                }
//
//                @Override
//                public int getBatchSize() {
//                    return batchList.size();
//                }
//            });
//
//            System.arraycopy(result, 0, affectedRows, i * batchSize, result.length);
//        }

        return ret;
    }

}
