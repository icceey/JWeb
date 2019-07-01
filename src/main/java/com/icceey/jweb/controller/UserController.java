package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.constants.UserType;
import com.icceey.jweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.*;


@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    private final int pageSize = 12;


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

        log.info("[register] " + regUser);

        Set<ConstraintViolation<User>> set = validator.validate(regUser, User.Login.class);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        if (userService.findUserByUserName(regUser.getUsername()) == null) {
            regUser.setPassword(BCrypt.hashpw(regUser.getPassword(), BCrypt.gensalt()));
            userService.saveUser(regUser.setType(UserType.USER));
            log.info("[register] " + regUser);
            return BaseResponse.success("注册成功");
        } else return BaseResponse.fail("用户名已存在");
    }


    @RequestMapping("/update/password")
    public BaseResponse changePassword(@RequestBody HashMap<String, String> mp, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        String orgPassword = mp.get("password");
        String password = mp.get("newPassword");

        if(BCrypt.checkpw(orgPassword, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            userService.saveUser(user);
            session.setAttribute(session.getId(), user);
            return BaseResponse.success();
        } else return BaseResponse.fail("密码错误");

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

        return BaseResponse.success().put("user", user);
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


    @RequestMapping("/all")
    public BaseResponse getAllUser(@RequestParam("page") Integer page, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        if(user.getType() >= UserType.ADMIN) {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<User> pages = userService.getAllUser(pageable);
            List<User> users = new ArrayList<>();
            pages.forEach(users::add);
            return BaseResponse.success()
                    .put("tot", pages.getTotalPages())
                    .put("users", users);
        } else {
            return BaseResponse.fail("权限不足");
        }

    }


    @RequestMapping("/delete/{id}")
    public BaseResponse deleteUser(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        User dUser = userService.findUserById(id);
        if(dUser == null) return BaseResponse.fail("用户不存在");
        if(user.getId().equals(dUser.getId())) return BaseResponse.fail("不可以删除自己哦");

        if(user.getType() >= dUser.getType()) {
            userService.deleteUser(dUser);
            log.info("[delete user] user [" + dUser.getUsername() + "] delete by [" + user.getUsername() + "]");
            return BaseResponse.success();
        } else {
            return BaseResponse.fail("权限不足");
        }
    }


    @RequestMapping("/update/type/{id}/{type}")
    public BaseResponse updateUserType(@PathVariable("id") Long id, @PathVariable("type") Integer type, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        User dUser = userService.findUserById(id);
        if(dUser == null) return BaseResponse.fail("用户不存在");
        if(user.getId().equals(dUser.getId())) return BaseResponse.fail("不可以操作自己哦");

        if(user.getType() >= dUser.getType() && user.getType() >= type) {
            dUser.setType(type);
            userService.saveUser(dUser);
            log.info("[update user] user [" + dUser.getUsername() + "] change by [" + user.getUsername() + "] to " + dUser.getType());
            return BaseResponse.success();
        } else {
            return BaseResponse.fail("权限不足");
        }
    }


}
