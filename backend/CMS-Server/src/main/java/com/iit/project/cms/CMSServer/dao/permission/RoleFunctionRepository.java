package com.iit.project.cms.CMSServer.dao.permission;

import com.iit.project.cms.CMSServer.dao.JdbcRepository;
import com.iit.project.cms.CMSServer.entity.permission.RoleFunction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleFunctionRepository extends JdbcRepository {
    public boolean createRoleFunction(RoleFunction roleFunction) {
        String sql = "INSERT INTO role_function (role_id, function_id) VALUES (?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, roleFunction.getRoleId(), roleFunction.getFunctionId());

        return rowsAffected > 0; // Returns true if at least one row is affected
    }

    public boolean deleteRoleFunction(Long roleId, Long functionId) {
        String sql = "DELETE FROM role_function WHERE role_id = ? AND function_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, roleId, functionId);

        return rowsAffected > 0; // Returns true if at least one row is affected
    }

    public boolean updateRoleFunction(RoleFunction roleFunction) {
        String sql = "UPDATE role_function SET role_id = ? WHERE function_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, roleFunction.getRoleId(), roleFunction.getFunctionId());

        return rowsAffected > 0; // Returns true if at least one row is affected
    }

    public RoleFunction getRoleFunctionByIds(Long roleId, Long functionId) {
        String sql = "SELECT * FROM role_function WHERE role_id = ? AND function_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{roleId, functionId}, BeanPropertyRowMapper.newInstance(RoleFunction.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // RoleFunction not found for the given ids, return null
        }
    }

    public List<RoleFunction> getAllRoleFunctions() {
        String sql = "SELECT * FROM role_function";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RoleFunction.class));
    }
}
