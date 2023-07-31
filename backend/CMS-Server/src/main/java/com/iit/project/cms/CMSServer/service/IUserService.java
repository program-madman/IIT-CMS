package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
import com.iit.project.cms.CMSServer.dto.FullDetailRequest;
import com.iit.project.cms.CMSServer.dto.LoginRequest;
import com.iit.project.cms.CMSServer.dto.RegisterRequest;
import com.iit.project.cms.CMSServer.entity.User;

public interface IUserService {

    BaseResponse login(LoginRequest request);

    BaseResponse logout();

    BaseResponse register(RegisterRequest request);

    BaseResponse getUsers();

    BaseResponse createUser(User user);

    BaseResponse getUserInfoById(Long userId);

    CommonResult queryUserArticleInfo(FullDetailRequest request);

    BaseResponse getAllUserTypes();

    BaseResponse getAllDepartments();
}
