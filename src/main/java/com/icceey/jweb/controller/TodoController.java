package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;


@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @RequestMapping("/all")
    public BaseResponse getTodos(HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        if(user != null) {
            List<Todo> todos = todoService.getAllTodosByOwnerId(user.getId());
            return BaseResponse.success().put("todos", todos);
        }
        return BaseResponse.sessionExpires();
    }

}
