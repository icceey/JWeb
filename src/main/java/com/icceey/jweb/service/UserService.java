package com.icceey.jweb.service;

import com.icceey.jweb.beans.User;
import com.icceey.jweb.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User findUserByUserName(String username) {
        return userDao.findUserByUserName(username);
    }

    public void insertUser(User user) {
        userDao.insert(user);
    }

}
