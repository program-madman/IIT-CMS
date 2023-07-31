package com.iit.project.cms.CMSServer.dao;


import com.iit.project.cms.CMSServer.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class UserRepository extends JdbcRepository {
    // 添加用户
    public boolean createUser(User user) {
        // 首先检查是否已存在具有相同用户名或邮箱的用户
        String checkExistingUserSql = "SELECT COUNT(*) FROM user WHERE user_name = ? OR mail = ?";
        int existingUserCount = jdbcTemplate.queryForObject(checkExistingUserSql, Integer.class, user.getUsername(), user.getMail());

        if (existingUserCount > 0) {
            // 如果已存在相同用户名或邮箱的用户，则不进行插入操作
            return false;
        }

        String createUserSql = "INSERT INTO user (dept_id, role_id, first_name, last_name, user_name, password, " +
                "register_time, avatar, user_type, gender, mail, address_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(createUserSql, user.getDeptId(), user.getRoleId(), user.getFirstName(),
                user.getLastName(), user.getUsername(), user.getPassword(), new Date(), user.getAvatar(),
                user.getUserType(), user.getGender(), user.getMail(), user.getAddressId());

        return rowsAffected > 0;
    }


    public User getUserById(Long userId) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(User.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到用户，返回null
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
    }

    // 更新用户
    public boolean updateUser(User user) {
        // 首先检查是否已存在具有相同用户名或邮箱的其他用户
        String checkExistingUserSql = "SELECT COUNT(*) FROM user WHERE user_id != ? AND (user_name = ? OR mail = ?)";
        int existingUserCount = jdbcTemplate.queryForObject(checkExistingUserSql, Integer.class, user.getUserId(), user.getUsername(), user.getMail());

        if (existingUserCount > 0) {
            // 如果已存在相同用户名或邮箱的其他用户，则不进行更新操作
            return false;
        }

        String updateUserSql = "UPDATE user SET dept_id = ?, role_id = ?, first_name = ?, last_name = ?, " +
                "user_name = ?, password = ?, register_time = ?, avatar = ?, user_type = ?, gender = ?, mail = ?, " +
                "address_id = ? WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(updateUserSql, user.getDeptId(), user.getRoleId(), user.getFirstName(),
                user.getLastName(), user.getUsername(), user.getPassword(), new Date(), user.getAvatar(),
                user.getUserType(), user.getGender(), user.getMail(), user.getAddressId(), user.getUserId());

        return rowsAffected > 0;
    }


    public boolean deleteUser(Long userId) {
        String sql = "DELETE FROM user WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, userId);

        return rowsAffected > 0; // 返回是否删除成功
    }

    public User getUserByUserName(String username) {
        String sql = "SELECT * FROM user WHERE user_name=?";
        List<User> result = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), username);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List<User> getUsersByDeptId(Long deptId) {
        String sql = "SELECT * FROM user where dept_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), deptId);
    }

    public User queryUserArticleInfo() {
        String sql = "SELECT * FROM user";
        List<User> result = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }


}
