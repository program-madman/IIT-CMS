package com.iit.project.cms.CMSServer.dao.permission;

import com.iit.project.cms.CMSServer.dao.JdbcRepository;
import com.iit.project.cms.CMSServer.entity.permission.Function;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FunctionRepository extends JdbcRepository {
    public boolean createFunction(Function function) {
        String sql = "INSERT INTO function (function_name, api) VALUES (?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, function.getFunctionName(), function.getApi());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Function getFunctionById(Long functionId) {
        String sql = "SELECT * FROM function WHERE function_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{functionId}, BeanPropertyRowMapper.newInstance(Function.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的功能，返回null
        }
    }

    public List<Function> getAllFunctions() {
        String sql = "SELECT * FROM function";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Function.class));
    }

    public boolean updateFunction(Function function) {
        String sql = "UPDATE function SET function_name = ?, api = ? WHERE function_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, function.getFunctionName(), function.getApi(), function.getFunctionId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteFunction(Long functionId) {
        String sql = "DELETE FROM function WHERE function_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, functionId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}
