package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.user.LoginRequest;
import com.iit.project.cms.CMSServer.dto.user.RegisterRequest;
import com.iit.project.cms.CMSServer.entity.user.User;

public interface IUserService {

    BaseResponse login(LoginRequest request);

    BaseResponse logout();

    BaseResponse register(RegisterRequest request);

    BaseResponse getUsers();

    BaseResponse createUser(User user);
}
