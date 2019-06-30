package com.icceey.jweb.service;

import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;


    @Override
    public List<Todo> getAllUndoByOwnerId(Long ownerId) {
        return todoRepository.findAllByOwnerIdAndDoneFalse(ownerId);
    }


    @Override
    public List<Todo> getAllDoneByOwnerId(Long ownerId) {
        return todoRepository.findAllByOwnerIdAndDoneTrue(ownerId);
    }


    @Override
    public List<Todo> getAllTodosByOwnerId(Long ownerId) {
        return todoRepository.findAllByOwnerId(ownerId);
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
