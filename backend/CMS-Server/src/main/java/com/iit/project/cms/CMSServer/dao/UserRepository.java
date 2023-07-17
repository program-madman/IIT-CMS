package com.iit.project.cms.CMSServer.dao;


import com.iit.project.cms.CMSServer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public int saveUser(User user) {
        String sql = "insert into user(id, name) values(?,?)";
        return jdbcTemplate.update(sql, user.getId(), user.getName());
    }

    public int deleteUserById(User user) {
        String sql = "delete from user where id = ?";
        return jdbcTemplate.update(sql, user.getId());
    }
}
