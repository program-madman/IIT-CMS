package com.iit.project.cms.CMSServer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
