package com.iit.project.cms.CMSServer.dao;


import com.iit.project.cms.CMSServer.entity.UserType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserTypeRepository extends JdbcRepository {

    public UserType createUserType(UserType userType) {
        String sql = "INSERT INTO user_type (type_name) VALUES (?)";
        int rowsAffected = jdbcTemplate.update(sql, userType.getTypeName());

        if (rowsAffected > 0) {
            Long typeId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            userType.setTypeId(typeId);
            return userType;
        } else {
            return null; // Return null if insertion fails
        }
    }

    public UserType getUserTypeById(Long typeId) {
        String sql = "SELECT * FROM user_type WHERE type_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{typeId}, BeanPropertyRowMapper.newInstance(UserType.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // Return null if no record is found
        }
    }

    public UserType getUserTypeByName(String typeName) {
        String sql = "SELECT * FROM user_type WHERE type_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{typeName}, BeanPropertyRowMapper.newInstance(UserType.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // Return null if no record is found
        }
    }

    public List<UserType> getAllUserTypes() {
        String sql = "SELECT * FROM user_type";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserType.class));
    }
}
