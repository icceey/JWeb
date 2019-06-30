package com.icceey.jweb.service;

import com.icceey.jweb.beans.User;

import java.util.List;


public interface UserService {

    User findUserById(Long id);

    User findUserByUserName(String username);

    List<User> getAllUser();

    void saveUser(User user);

}
