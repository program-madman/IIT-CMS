package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.entity.User;

import java.util.Collection;
import java.util.List;

public interface IUserService {

    List<User> getUsers();
    int createUser(User user);
}
