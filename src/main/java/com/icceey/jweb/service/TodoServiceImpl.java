package com.icceey.jweb.service;

import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;


    @Override
    public Page<Todo> getAllUndoByOwnerId(Long ownerId, Pageable pageable) {
        return todoRepository.findAllByOwnerIdAndDoneFalse(ownerId, pageable);
    }


    @Override
    public Page<Todo> getAllDoneByOwnerId(Long ownerId, Pageable pageable) {
        return todoRepository.findAllByOwnerIdAndDoneTrue(ownerId, pageable);
    }


    @Override
    public Page<Todo> getAllTodosByOwnerId(Long ownerId, Pageable pageable) {
        return todoRepository.findAllByOwnerId(ownerId, pageable);
    }


    @Transactional
    @Override
    public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }


    @Override
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id).get();
    }


    @Transactional
    @Override
    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }


}
