package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class GetUserInfoResponse {
    private Long userId;

    private Long deptId;

    private Long roleId;

    private String firstName;

    private String lastName;

    private String username;

    private Integer age;

    private String phoneNumber;

    private String houseNumber;

    private Date registerTime;

    private String avatar;

    private String userType;

    private String gender;

    private String country;

    private String state;

    private String city;

    private String street;

    private String mail;
}
