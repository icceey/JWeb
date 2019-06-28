package com.icceey.jweb.service;

import com.icceey.jweb.beans.User;
import com.icceey.jweb.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }


    public User findUserByUserName(String username) {
        return userDao.findUserByUserName(username);
    }

    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    public void insertUser(User user) {
        userDao.insert(user);
    }

    public void updateUserInfo(User user) {
        userDao.updateUserInfo(user);
    }

    public void updateUserAvatar(User user) {
        userDao.updateUserAvatar(user);
    }

}
