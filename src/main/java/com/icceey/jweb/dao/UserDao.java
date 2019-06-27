package com.icceey.jweb.dao;


import com.icceey.jweb.beans.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserDao {

    @Select("select * from user where username=#{username}")
    User findUserByUserName(@Param("username") String username);


    @Insert("insert into user(username, password, avatar, mail, nickname, phone) " +
            "values(#{username},#{password},#{avatar},#{mail},#{nickname},#{phone})")
    void insert(User user);




}
