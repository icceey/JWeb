package com.icceey.jweb.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user")
public class User implements Serializable {

    @NotNull
    @Range
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(groups = {Login.class}, message = "用户名不能为空")
    @Length(groups = {Login.class}, max = 20, message = "用户名过长")
    private String username;

    @NotBlank(groups = {Login.class}, message = "密码不能为空")
    @Length(groups = {Login.class}, max = 60, message = "密码过长")
    private String password;

    @Length(groups = {Profile.class}, max = 50, message = "昵称过长")
    private String nickname;

    @Length(max = 50, message = "头像路径过长")
    private String avatar;

    @Email(groups = {Profile.class})
    @Length(groups = {Profile.class}, max = 30, message = "邮箱过长")
    private String mail;

    @Length(groups = {Profile.class}, max = 15, message = "手机号过长")
    private String phone;


    private Integer type;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public interface Login { }

    @GroupSequence({Login.class, Profile.class})
    public interface Register{ }

    public interface Profile{ }

}
