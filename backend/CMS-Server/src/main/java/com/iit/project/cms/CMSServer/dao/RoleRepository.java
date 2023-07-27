package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository extends JdbcRepository {
    public boolean createRole(Role role) {
        String sql = "INSERT INTO role (role_name) VALUES (?)";

        int rowsAffected = jdbcTemplate.update(sql, role.getRoleName());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Role getRoleById(Long roleId) {
        String sql = "SELECT * FROM role WHERE role_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{roleId}, BeanPropertyRowMapper.newInstance(Role.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的角色，返回null
        }
    }

    public List<Role> getAllRoles() {
        String sql = "SELECT * FROM role";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Role.class));
    }

    public boolean updateRole(Role role) {
        String sql = "UPDATE role SET role_name = ? WHERE role_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, role.getRoleName(), role.getRoleId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteRole(Long roleId) {
        String sql = "DELETE FROM role WHERE role_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, roleId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}
