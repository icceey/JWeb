package com.icceey.jweb.dao;


import com.icceey.jweb.beans.Todo;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface TodoDao {

    @Select("select * from todo where owner_id=#{owner}")
    List<Todo> getAllTodosByOwnerId(@Param("owner") Integer ownerId);


    @Delete("delete from todo where id=#{id}")
    void deleteTodoById(@Param("id") Integer id);


    @Insert("insert into todo(title, content, owner_id, owner_type, datetime) " +
            "values(#{title}, #{content}, #{ownerId}, #{ownerType}, #{datetime})")
    void insertTodo(Todo todo);

}
