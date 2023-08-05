package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.dto.GetMessageResponse;
import com.iit.project.cms.CMSServer.dto.GetUserInfoResponse;
import com.iit.project.cms.CMSServer.entity.Message;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class MessageRepository extends JdbcRepository {

    // 添加消息
    public boolean createMessage(Message message) {
        String sqlSelectMaxId = "SELECT MAX(message_id) FROM message";
        Integer maxId = jdbcTemplate.queryForObject(sqlSelectMaxId, Integer.class);

        // Increment the maxId by 1 to get the new message_id
        int newMessageId = (maxId != null) ? (maxId + 1) : 1;

        String sql = "INSERT INTO message (message_id, from_user, to_user, title, content, send_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";


        int rowsAffected = jdbcTemplate.update(sql, newMessageId, message.getFromUser(), message.getToUser(),
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

    // 获取所有消息
    public List<GetMessageResponse> getAllMessagesOneSql() {
        String sql = "SELECT " +
                "m.message_id AS messageId, " +
                "m.from_user AS fromUser, " +
                "CONCAT(fu.first_name, ' ', fu.last_name) AS fromUserName, " +
                "m.to_user AS toUser, " +
                "CONCAT(tu.first_name, ' ', tu.last_name) AS toUserName, " +
                "m.title, " +
                "m.content, " +
                "m.send_time AS sendTime, " +
                "fu.user_id AS fromUserInfo_userId, " +
                "fu.dept_id AS fromUserInfo_deptId, " +
                "fu.role_id AS fromUserInfo_roleId, " +
                "fu.first_name AS fromUserInfo_firstName, " +
                "fu.last_name AS fromUserInfo_lastName, " +
                "fu.username AS fromUserInfo_username, " +
                "fu.age AS fromUserInfo_age, " +
                "fu.phone_number AS fromUserInfo_phoneNumber, " +
                "fu.house_number AS fromUserInfo_houseNumber, " +
                "fu.register_time AS fromUserInfo_registerTime, " +
                "fu.avatar AS fromUserInfo_avatar, " +
                "fu.user_type AS fromUserInfo_userType, " +
                "fu.gender AS fromUserInfo_gender, " +
                "fu.country AS fromUserInfo_country, " +
                "fu.state AS fromUserInfo_state, " +
                "fu.city AS fromUserInfo_city, " +
                "fu.street AS fromUserInfo_street, " +
                "fu.mail AS fromUserInfo_mail, " +
                "tu.user_id AS toUserInfo_userId, " +
                "tu.dept_id AS toUserInfo_deptId, " +
                "tu.role_id AS toUserInfo_roleId, " +
                "tu.first_name AS toUserInfo_firstName, " +
                "tu.last_name AS toUserInfo_lastName, " +
                "tu.username AS toUserInfo_username, " +
                "tu.age AS toUserInfo_age, " +
                "tu.phone_number AS toUserInfo_phoneNumber, " +
                "tu.house_number AS toUserInfo_houseNumber, " +
                "tu.register_time AS toUserInfo_registerTime, " +
                "tu.avatar AS toUserInfo_avatar, " +
                "tu.user_type AS toUserInfo_userType, " +
                "tu.gender AS toUserInfo_gender, " +
                "tu.country AS toUserInfo_country, " +
                "tu.state AS toUserInfo_state, " +
                "tu.city AS toUserInfo_city, " +
                "tu.street AS toUserInfo_street, " +
                "tu.mail AS toUserInfo_mail " +
                "FROM message m " +
                "LEFT JOIN user fu ON m.from_user = fu.user_id " +
                "LEFT JOIN user tu ON m.to_user = tu.user_id";

        List<GetMessageResponse> responseList = jdbcTemplate.query(sql, (rs) -> {
            List<GetMessageResponse> messages = new ArrayList<>();
            while (rs.next()) {
                GetMessageResponse message = new GetMessageResponse();
                message.setMessageId(rs.getLong("messageId"));
                message.setFromUser(rs.getLong("fromUser"));
                message.setFromUserName(rs.getString("fromUserName"));
                message.setToUser(rs.getLong("toUser"));
                message.setToUserName(rs.getString("toUserName"));
                message.setTitle(rs.getString("title"));
                message.setContent(rs.getString("content"));
                message.setSendTime(rs.getDate("sendTime"));

                GetUserInfoResponse fromUserInfo = new GetUserInfoResponse();
                fromUserInfo.setUserId(rs.getLong("fromUserInfo_userId"));
                fromUserInfo.setDeptId(rs.getLong("fromUserInfo_deptId"));
                fromUserInfo.setRoleId(rs.getLong("fromUserInfo_roleId"));
                fromUserInfo.setFirstName(rs.getString("fromUserInfo_firstName"));
                fromUserInfo.setLastName(rs.getString("fromUserInfo_lastName"));
                fromUserInfo.setUsername(rs.getString("fromUserInfo_username"));
                fromUserInfo.setAge(rs.getInt("fromUserInfo_age"));
                fromUserInfo.setPhoneNumber(rs.getString("fromUserInfo_phoneNumber"));
                fromUserInfo.setHouseNumber(rs.getString("fromUserInfo_houseNumber"));
                fromUserInfo.setRegisterTime(rs.getDate("fromUserInfo_registerTime"));
                fromUserInfo.setAvatar(rs.getString("fromUserInfo_avatar"));
                fromUserInfo.setUserType(rs.getString("fromUserInfo_userType"));
                fromUserInfo.setGender(rs.getString("fromUserInfo_gender"));
                fromUserInfo.setCountry(rs.getString("fromUserInfo_country"));
                fromUserInfo.setState(rs.getString("fromUserInfo_state"));
                fromUserInfo.setCity(rs.getString("fromUserInfo_city"));
                fromUserInfo.setStreet(rs.getString("fromUserInfo_street"));
                fromUserInfo.setMail(rs.getString("fromUserInfo_mail"));
                message.setFromUserInfo(fromUserInfo);

                GetUserInfoResponse toUserInfo = new GetUserInfoResponse();
                toUserInfo.setUserId(rs.getLong("toUserInfo_userId"));
                toUserInfo.setDeptId(rs.getLong("toUserInfo_deptId"));
                toUserInfo.setRoleId(rs.getLong("toUserInfo_roleId"));
                toUserInfo.setFirstName(rs.getString("toUserInfo_firstName"));
                toUserInfo.setLastName(rs.getString("toUserInfo_lastName"));
                toUserInfo.setUsername(rs.getString("toUserInfo_username"));
                toUserInfo.setAge(rs.getInt("toUserInfo_age"));
                toUserInfo.setPhoneNumber(rs.getString("toUserInfo_phoneNumber"));
                toUserInfo.setHouseNumber(rs.getString("toUserInfo_houseNumber"));
                toUserInfo.setRegisterTime(rs.getDate("toUserInfo_registerTime"));
                toUserInfo.setAvatar(rs.getString("toUserInfo_avatar"));
                toUserInfo.setUserType(rs.getString("toUserInfo_userType"));
                toUserInfo.setGender(rs.getString("toUserInfo_gender"));
                toUserInfo.setCountry(rs.getString("toUserInfo_country"));
                toUserInfo.setState(rs.getString("toUserInfo_state"));
                toUserInfo.setCity(rs.getString("toUserInfo_city"));
                toUserInfo.setStreet(rs.getString("toUserInfo_street"));
                toUserInfo.setMail(rs.getString("toUserInfo_mail"));
                message.setToUserInfo(toUserInfo);

                messages.add(message);
            }
            return messages;
        });


        return responseList;

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
            ret = createMessage(message);
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
