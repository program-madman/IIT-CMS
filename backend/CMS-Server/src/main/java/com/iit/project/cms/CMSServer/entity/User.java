package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "dept_id", nullable = false)
    private Long deptId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "register_time", nullable = false)
    private Date registerTime;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "address_id", nullable = false)
    private Long addressId;
}

