package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CommonResult;
import com.iit.project.cms.CMSServer.dto.FullDetailRequest;
import com.iit.project.cms.CMSServer.dto.GetUserInfoRequest;
import com.iit.project.cms.CMSServer.dto.LoginRequest;
import com.iit.project.cms.CMSServer.dto.RegisterRequest;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/getUserInfoById")
    private BaseResponse getUserInfoById(@RequestBody GetUserInfoRequest request) {
        return userService.getUserInfoById(request.getUserId());
    }

    @RequestMapping("/createUser")
    private BaseResponse createUser(@RequestParam long id, @RequestParam String name) {
        User user = new User();
        log.info(user.toString());
        return userService.createUser(user);
    }


    @RequestMapping(value = "/queryUserArticleInfo")
    public CommonResult queryUserArticleInfo(@RequestBody FullDetailRequest request) {

        return userService.queryUserArticleInfo(request);
    }

    @RequestMapping(value = "/getAllUserTypes")
    public BaseResponse getAllUserTypes() {
        return userService.getAllUserTypes();
    }

    @RequestMapping(value = "/getAllDepartments")
    public BaseResponse getAllDepartments() {
        return userService.getAllDepartments();
    }

}
