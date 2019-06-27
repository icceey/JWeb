package com.icceey.jweb.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名过长")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(max = 60, message = "密码过长")
    private String password;

    @Length(max = 50, message = "昵称过长")
    private String nickname;

    @Length(max = 50, message = "头像路径过长")
    private String avatar;

    @Email
    @Length(max = 30, message = "邮箱过长")
    private String mail;


    @Length(max = 15, message = "手机号过长")
    private String phone;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
