package com.icceey.jweb.service;

import com.icceey.jweb.beans.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TodoService {

    Page<Todo> getAllUndoByOwnerId(Long ownerId, Pageable pageable);

    Page<Todo> getAllDoneByOwnerId(Long ownerId, Pageable pageable);

    Page<Todo> getAllTodosByOwnerId(Long ownerId, Pageable pageable);

    void deleteTodoById(Long id);

    Todo getTodoById(Long id);

    void saveTodo(Todo todo);


}
