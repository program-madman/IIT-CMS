package com.iit.project.cms.CMSServer.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
