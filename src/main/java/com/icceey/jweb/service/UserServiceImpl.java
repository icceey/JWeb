package com.icceey.jweb.service;

import com.icceey.jweb.beans.User;
import com.icceey.jweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }


    @Override
    public User findUserByUserName(String username) {
        return userRepository.findUserByUsername(username);
    }


    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }


    @Transactional
    @Override
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}
