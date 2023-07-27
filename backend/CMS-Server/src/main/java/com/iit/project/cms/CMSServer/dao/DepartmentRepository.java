package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.dao.JdbcRepository;
import com.iit.project.cms.CMSServer.entity.Department;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentRepository extends JdbcRepository {
    public boolean createDepartment(Department department) {
        String sql = "INSERT INTO department (dept_name) VALUES (?)";

        int rowsAffected = jdbcTemplate.update(sql, department.getDeptName());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Department getDepartmentById(Long deptId) {
        String sql = "SELECT * FROM department WHERE dept_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{deptId}, BeanPropertyRowMapper.newInstance(Department.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的部门，返回null
        }
    }

    public Department getDepartmentByName(String deptName) {
        String sql = "SELECT * FROM department WHERE dept_name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{deptName}, BeanPropertyRowMapper.newInstance(Department.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的部门，返回null
        }
    }

    public List<Department> getAllDepartments() {
        String sql = "SELECT * FROM department";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Department.class));
    }

    public boolean updateDepartment(Department department) {
        String sql = "UPDATE department SET dept_name = ? WHERE dept_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, department.getDeptName(), department.getDeptId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteDepartment(Long deptId) {
        String sql = "DELETE FROM department WHERE dept_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, deptId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}

