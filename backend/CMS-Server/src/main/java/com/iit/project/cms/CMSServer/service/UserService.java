package com.iit.project.cms.CMSServer.service;


import com.iit.project.cms.CMSServer.dao.UserRepository;
import com.iit.project.cms.CMSServer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public int createUser(User user) {
        return userRepository.saveUser(user);
    }


}
