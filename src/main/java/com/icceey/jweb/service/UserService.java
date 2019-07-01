package com.icceey.jweb.service;

import com.icceey.jweb.beans.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    User findUserById(Long id);

    User findUserByUserName(String username);

    Page<User> getAllUser(Pageable pageable);

    void saveUser(User user);

    void deleteUser(User user);

}
