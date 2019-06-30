package com.icceey.jweb.service;

import com.icceey.jweb.beans.Todo;

import java.util.List;


public interface TodoService {

    List<Todo> getAllUndoByOwnerId(Long ownerId);

    List<Todo> getAllDoneByOwnerId(Long ownerId);

    List<Todo> getAllTodosByOwnerId(Long ownerId);

    void deleteTodoById(Long id);

    Todo getTodoById(Long id);

    void saveTodo(Todo todo);


}
