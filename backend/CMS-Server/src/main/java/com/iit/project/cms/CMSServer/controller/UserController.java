package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.user.LoginRequest;
import com.iit.project.cms.CMSServer.dto.user.RegisterRequest;
import com.iit.project.cms.CMSServer.entity.user.User;
import com.iit.project.cms.CMSServer.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public BaseResponse login(@RequestBody @Validated LoginRequest request) {
        return userService.login(request);
    }

    @RequestMapping("/logout")
    public BaseResponse logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @RequestMapping("/register")
    public BaseResponse register(@RequestBody @Validated RegisterRequest request) {
        return userService.register(request);
    }

    @RequestMapping("/getAllUsers")
    private BaseResponse getAllUsers() {
        return BaseResponse.success(userService.getUsers());
    }

    @RequestMapping("/createUser")
    private BaseResponse createUser(@RequestParam long id, @RequestParam String name) {
        User user = new User();
        log.info(user.toString());
        return userService.createUser(user);
    }
}
