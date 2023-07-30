package com.iit.project.cms.CMSServer.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class
RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String passwordRepeat;
    @NotNull
    private Long deptId;
    @NotNull
    private Long roleId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String userType;
    @NotNull
    private String gender;
    @NotNull
    private String mail;
}
