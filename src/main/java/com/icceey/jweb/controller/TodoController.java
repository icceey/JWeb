package com.icceey.jweb.controller;

import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.Todo;
import com.icceey.jweb.beans.User;
import com.icceey.jweb.constants.OwnerType;
import com.icceey.jweb.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;


@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private Validator validator;

    private final int pageSize = 12;


    @RequestMapping("/all")
    public BaseResponse getTodos(@RequestParam("page") Integer page, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Todo> pages = todoService.getAllTodosByOwnerId(user.getId(), pageable);
        Integer totalPages = pages.getTotalPages();
        List<Todo> todos = new ArrayList<>();
        pages.forEach(todos::add);
        return BaseResponse.success().put("tot", totalPages).put("todos", todos);
    }


    @RequestMapping("/all/undo")
    public BaseResponse getUndo(@RequestParam("page") Integer page, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Todo> pages = todoService.getAllUndoByOwnerId(user.getId(), pageable);
        Integer totalPages = pages.getTotalPages();
        List<Todo> todos = new ArrayList<>();
        pages.forEach(todos::add);
        return BaseResponse.success().put("tot", totalPages).put("todos", todos);
    }


    @RequestMapping("/all/done")
    public BaseResponse getDone(@RequestParam("page") Integer page, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Todo> pages = todoService.getAllDoneByOwnerId(user.getId(), pageable);
        Integer totalPages = pages.getTotalPages();
        List<Todo> todos = new ArrayList<>();
        pages.forEach(todos::add);
        return BaseResponse.success().put("tot", totalPages).put("todos", todos);
    }


    @RequestMapping("/update/done/{id}")
    public BaseResponse doneTodo(@PathVariable("id") Long id, HttpSession session) {
        return setDoneTodo(id, session, true);
    }


    @RequestMapping("/update/undo/{id}")
    public BaseResponse undoTodo(@PathVariable("id") Long id, HttpSession session) {
        return setDoneTodo(id, session, false);
    }


    @RequestMapping("/add")
    public BaseResponse addTodo(@RequestBody Todo todo, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        Set<ConstraintViolation<Todo>> set = validator.validate(todo);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        log.info("[add todo] user:" + user.getUsername() + " title:" + todo.getTitle());
        todo.setOwnerId(user.getId()).setOwnerType(OwnerType.USER);
        todoService.saveTodo(todo);

        log.info("[add todo] " + todo);
        return BaseResponse.success().put("todo", todo);
    }


    @RequestMapping("/update/{id}")
    public BaseResponse updateTodo(@PathVariable("id") Long id, @RequestBody Todo updTodo, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        Set<ConstraintViolation<Todo>> set = validator.validate(updTodo);
        if(!set.isEmpty()) return BaseResponse.fail(set.iterator().next().getMessage());

        Todo todo = todoService.getTodoById(id);

        log.info("[update todo] user:" + user.getUsername() + " org-title:" + todo.getTitle());
        todo.setTitle(updTodo.getTitle())
                .setContent(updTodo.getContent())
                .setDatetime(updTodo.getDatetime());
        todoService.saveTodo(todo);
        log.info("[update todo] new:" + todo);

        return BaseResponse.success().put("todo", todo);
    }



    @RequestMapping("/delete/{id}")
    public BaseResponse deleteTodo(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(session.getId());

        Todo todo = todoService.getTodoById(id);
        if(todo == null) return BaseResponse.fail("该备忘不存在");

        if(todo.getOwnerType() == OwnerType.USER) {
            if(user.getId().equals(todo.getOwnerId())) {
                log.info("[delete todo] user:" + user.getUsername() + " title:" + todo.getTitle());
                todoService.deleteTodoById(id);
            } else {
                return BaseResponse.fail("这个备忘似乎不是您的");
            }
        } else {

        }
        return BaseResponse.success();
    }




    private BaseResponse setDoneTodo(Long id, HttpSession session, boolean done) {
        User user = (User) session.getAttribute(session.getId());

        Todo todo = todoService.getTodoById(id);
        if(todo == null) return BaseResponse.fail("该备忘不存在");

        if(todo.getOwnerType() == OwnerType.USER) {
            if(user.getId().equals(todo.getOwnerId())) {
                log.info("[set todo done] user:" + user.getUsername() + " title:" + todo.getTitle() + " --> " + done);
                todoService.saveTodo(todo.setDone(done));
            } else {
                return BaseResponse.fail("这个备忘似乎不是您的");
            }
        } else {

        }
        return BaseResponse.success();
    }

}
