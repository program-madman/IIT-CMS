package com.iit.project.cms.CMSServer.service;


import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.user.UserRepository;
import com.iit.project.cms.CMSServer.dto.user.LoginRequest;
import com.iit.project.cms.CMSServer.dto.user.LoginResponse;
import com.iit.project.cms.CMSServer.dto.user.RegisterRequest;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.util.CmsPasswordEncoder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.iit.project.cms.CMSServer.common.ExceptionEnum.*;
import static com.iit.project.cms.CMSServer.util.TokenUtil.generateToken;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

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

}
