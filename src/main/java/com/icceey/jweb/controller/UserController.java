package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Set;


@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;


    @RequestMapping("/login")
    public BaseResponse login(@RequestBody HashMap<String, String> mp, HttpSession session) {
        String username = mp.get("username");
        String password = mp.get("password");

        Set<ConstraintViolation<User>> set = validator.validate(new User(username, password));
        log.info("[login] ConstraintViolationSetSize: " + set.size());
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        User user = userService.findUserByUserName(username);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                session.setAttribute(session.getId(), user);
                log.info("[login] session-id: " + session.getId());
                log.info("[login] user: " + session.getAttribute(session.getId()));
                return BaseResponse.success("登录成功").put("user", user);
            } else return BaseResponse.fail("密码错误");
        } else return BaseResponse.fail("用户不存在");

    }


    @RequestMapping("/register")
    public BaseResponse register(@RequestBody HashMap<String, String> mp) {
        String username = mp.get("username");
        String password = mp.get("password");

        Set<ConstraintViolation<User>> set = validator.validate(new User(username, password));
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        if (userService.findUserByUserName(username) == null) {
            userService.insertUser(new User(username, BCrypt.hashpw(password, BCrypt.gensalt())));
            return BaseResponse.success("注册成功");
        } else return BaseResponse.fail("用户名已存在");
    }


    @RequestMapping("/logout")
    public BaseResponse logout(HttpSession session) {
        session.invalidate();
        return BaseResponse.success();
    }


    @RequestMapping("/userinfo")
    public BaseResponse getUser(HttpSession session)  {
        User user = (User) session.getAttribute(session.getId());
        if(user != null) {
            return BaseResponse.success().put("user", user);
        }
        return BaseResponse.fail();
    }


}
