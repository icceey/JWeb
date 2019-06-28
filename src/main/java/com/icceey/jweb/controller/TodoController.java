package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.constants.OwnerType;
import com.icceey.jweb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;


@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private Validator validator;


    @RequestMapping("/all")
    public BaseResponse getTodos(HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        List<Todo> todos = todoService.getAllTodosByOwnerId(user.getId());
        return BaseResponse.success().put("todos", todos);
    }


    @RequestMapping("/add")
    public BaseResponse addTodo(@RequestBody Todo todo, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        Set<ConstraintViolation<Todo>> set = validator.validate(todo);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        todo.setOwnerId(user.getId()).setOwnerType(OwnerType.USER);
        todoService.insertTodo(todo);

        return BaseResponse.success();
    }

}
