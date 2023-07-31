package com.iit.project.cms.CMSServer.service;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
import com.iit.project.cms.CMSServer.dao.DepartmentRepository;
import com.iit.project.cms.CMSServer.dao.UserRepository;
import com.iit.project.cms.CMSServer.dao.UserTypeRepository;
import com.iit.project.cms.CMSServer.dto.*;
import com.iit.project.cms.CMSServer.entity.Department;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.entity.UserType;
import com.iit.project.cms.CMSServer.util.CmsPasswordEncoder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.iit.project.cms.CMSServer.common.ExceptionEnum.*;
import static com.iit.project.cms.CMSServer.util.TokenUtil.generateToken;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public BaseResponse getUsers() {
        return BaseResponse.success(userRepository.getAllUsers());
    }

    @Override
    public BaseResponse createUser(User user) {
        boolean success = userRepository.createUser(user);
        if (success) {
            return BaseResponse.success();
        }
        return BaseResponse.error("create user failed");
    }

    @Override
    public BaseResponse getUserInfoById(Long userId) {
        User user = userRepository.getUserById(userId);
        GetUserInfoResponse response = new GetUserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return BaseResponse.success(response);
    }


    @Override
    public BaseResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        User user = userRepository.getUserByUserName(username);
        if (!user.getPassword().equals(CmsPasswordEncoder.encode(password))) {
            return BaseResponse.error(ACCOUNT_LOGIN_FAILED);
        }
        String token = generateToken(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return BaseResponse.success(loginResponse);
    }

    @Override
    public BaseResponse logout() {
        return null;
    }

    @Override
    public BaseResponse register(RegisterRequest request) {
        String password = request.getPassword();
        String passwordRepeat = request.getPasswordRepeat();
        if (!passwordRepeat.equals(password)) {
            return BaseResponse.error(ACCOUNT_PASSWORD_REPEAT_DIFF);
        }
        String passwordEncoded = CmsPasswordEncoder.encode(password);
        request.setPassword(passwordEncoded);
        User user = userRepository.getUserByUserName(request.getUsername());
        if (ObjectUtils.isNotEmpty(user)) {
            return BaseResponse.error(ACCOUNT_USER_NAME_EXIST);
        }
        User targetUser = new User();
        BeanUtils.copyProperties(request, targetUser);

        return BaseResponse.success(userRepository.createUser(targetUser));
    }


    @Override
    public CommonResult queryUserArticleInfo(FullDetailRequest request) {
        CommonResult result = new CommonResult();
        result.setResult(userRepository.queryUserArticleInfo());
        result.setSuccess(true);
        return result;
    }

    @Override
    public BaseResponse getAllUserTypes() {
        List<UserType> allUserTypes = userTypeRepository.getAllUserTypes();
        List<GetAllUserTypeResponse> responseList = new ArrayList<>();
        for (UserType userType : allUserTypes) {
            GetAllUserTypeResponse response = new GetAllUserTypeResponse();
            BeanUtils.copyProperties(userType, response);
            responseList.add(response);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse getAllDepartments() {
        List<Department> allDepartments = departmentRepository.getAllDepartments();
        List<GetAllDepartmentResponse> responseList = new ArrayList<>();
        for (Department department : allDepartments) {
            GetAllDepartmentResponse response = new GetAllDepartmentResponse();
            response.setDeptId(department.getDeptId());
            response.setDeptName(department.getDeptName());
            responseList.add(response);
        }
        return BaseResponse.success(responseList);
    }

}
