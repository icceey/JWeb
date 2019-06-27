package com.icceey.jweb.service;

import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.dao.TodoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoDao todoDao;


    public List<Todo> getAllTodosByOwnerId(Integer ownerId) {
        return todoDao.getAllTodosByOwnerId(ownerId);
    }


    public void deleteTodoById(Integer id) {
        todoDao.deleteTodoById(id);
    }


    public void insertTodo(Todo todo) {
        todoDao.insertTodo(todo);
    }


}
