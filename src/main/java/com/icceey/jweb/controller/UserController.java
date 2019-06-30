package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
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
    public BaseResponse login(@RequestBody User loginUser, HttpSession session) {
        Set<ConstraintViolation<User>> set = validator.validate(loginUser, User.Login.class);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        User user = userService.findUserByUserName(loginUser.getUsername());
        if (user != null) {
            if (BCrypt.checkpw(loginUser.getPassword(), user.getPassword())) {
                session.setAttribute(session.getId(), user);
                log.info("[login] session-id: " + session.getId());
                log.info("[login] user: " + session.getAttribute(session.getId()));
                return BaseResponse.success("登录成功").put("user", user);
            } else return BaseResponse.fail("密码错误");
        } else return BaseResponse.fail("用户不存在");

    }


    @RequestMapping("/register")
    public BaseResponse register(@RequestBody User regUser) {

        Set<ConstraintViolation<User>> set = validator.validate(regUser, User.Register.class);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        if (userService.findUserByUserName(regUser.getUsername()) == null) {
            userService.saveUser(regUser.setPassword(BCrypt.hashpw(regUser.getPassword(), BCrypt.gensalt())));
            log.info("[register] " + regUser);
            return BaseResponse.success("注册成功");
        } else return BaseResponse.fail("用户名已存在");
    }


    @RequestMapping("/profile/update")
    public BaseResponse updateUserInfo(@RequestBody HashMap<String, String> mp, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        user.setNickname(mp.get("nickname"))
                .setMail(mp.get("mail"))
                .setPhone(mp.get("phone"));

        Set<ConstraintViolation<User>> set = validator.validate(user, User.Profile.class);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        userService.saveUser(user);
        session.setAttribute(session.getId(), user);
        log.info("[update user info] " + user);
        return BaseResponse.success().put("user", user);

    }


    @RequestMapping("/avatar/update")
    public BaseResponse updateAvatar(@RequestParam("avatar") @NotNull MultipartFile file, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(session.getId());

        log.info("[update avatar] user:" + user.getUsername() + " file-type:" + file.getContentType() + " size:" + file.getSize() + " ");
        String msg = null;
        String orgName = file.getOriginalFilename();
        if(!Objects.requireNonNull(file.getContentType()).startsWith("image")) msg = "文件格式不支持";
        else if(file.getSize() > 5*1024*1024) msg = "图片不能大于5M";
        else if(orgName == null || orgName.isEmpty()) msg = "文件格式不合法";
        if(msg != null) return BaseResponse.fail(msg);

        String path = ResourceUtils.getURL("classpath:").getPath() + "user/avatar/";
        String name = user.getUsername() + "-" + System.currentTimeMillis();
        String suffix = orgName.substring(orgName.lastIndexOf("."));

        userService.saveUser(user.setAvatar(name+suffix));
        session.setAttribute(session.getId(), user);

        file.transferTo(new File(path + name + suffix));

        return BaseResponse.success();
    }


    @RequestMapping("/logout")
    public BaseResponse logout(HttpSession session) {
        session.invalidate();
        return BaseResponse.success();
    }


    @RequestMapping("/profile")
    public BaseResponse getUser(HttpSession session)  {
        User user = (User) session.getAttribute(session.getId());
        return BaseResponse.success().put("user", user);
    }


}
