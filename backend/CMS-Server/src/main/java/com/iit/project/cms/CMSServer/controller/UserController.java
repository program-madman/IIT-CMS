package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/getAllUsers")
    private BaseResponse getAllUsers() {
        return BaseResponse.success(userService.getUsers());
    }

    @RequestMapping("/createUser")
    private BaseResponse createUser(@RequestParam long id, @RequestParam String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        log.info(user.toString());
        int result = userService.createUser(user);
        log.info("result : " + result);
        return null;
    }
}
