package com.iit.project.cms.CMSServer.dto.user;

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
}
