package com.icceey.jweb.dao;


import com.icceey.jweb.beans.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface UserDao {


    @Select("select * from user where id=#{id}")
    User findUserById(@Param("id") Integer id);


    @Select("select * from user where username=#{username}")
    User findUserByUserName(@Param("username") String username);


    @Select("select * from user")
    List<User> getAllUser();


    @Insert("insert into user(username, password, avatar, mail, nickname, phone) " +
            "values(#{username},#{password},#{avatar},#{mail},#{nickname},#{phone})")
    void insert(User user);


    @Update("update user set nickname=#{nickname}, mail=#{mail}, phone=#{phone} where username=#{username}")
    void updateUserInfo(User user);

    @Update("update user set avatar=#{avatar} where username=#{username}")
    void updateUserAvatar(User user);

}
