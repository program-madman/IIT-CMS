package com.iit.project.cms.CMSServer.dao.user;


import com.iit.project.cms.CMSServer.dao.JdbcRepository;
import com.iit.project.cms.CMSServer.entity.user.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends JdbcRepository {
    public boolean createUser(User user) {
        String sql = "INSERT INTO user (dept_id, role_id, first_name, last_name, sign_up, user_name, password, " +
                "age, phone_number, house_number, register_time, avatar, user_type, gender, country, state, " +
                "city, street, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, user.getDeptId(), user.getRoleId(), user.getFirstName(),
                user.getLastName(), user.getSignUp(), user.getUsername(), user.getPassword(), user.getAge(),
                user.getPhoneNumber(), user.getHouseNumber(), user.getRegisterTime(), user.getAvatar(),
                user.getUserType(), user.getGender(), user.getCountry(), user.getState(), user.getCity(),
                user.getStreet(), user.getMail());

        return rowsAffected > 0; // 返回是否插入成功
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

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET dept_id = ?, role_id = ?, first_name = ?, last_name = ?, sign_up = ?, " +
                "user_name = ?, password = ?, age = ?, phone_number = ?, house_number = ?, register_time = ?, " +
                "avatar = ?, user_type = ?, gender = ?, country = ?, state = ?, city = ?, street = ?, mail = ? " +
                "WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, user.getDeptId(), user.getRoleId(), user.getFirstName(),
                user.getLastName(), user.getSignUp(), user.getUsername(), user.getPassword(), user.getAge(),
                user.getPhoneNumber(), user.getHouseNumber(), user.getRegisterTime(), user.getAvatar(),
                user.getUserType(), user.getGender(), user.getCountry(), user.getState(), user.getCity(),
                user.getStreet(), user.getMail(), user.getUserId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteUser(Long userId) {
        String sql = "DELETE FROM user WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, userId);

        return rowsAffected > 0; // 返回是否删除成功
    }

    public User getUserByUserName(String username) {
        String sql = "select * from user where user_name=?";
        List<User> result = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), username);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
