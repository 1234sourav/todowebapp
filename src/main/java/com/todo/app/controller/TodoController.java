package com.todo.app.controller;

import com.todo.app.exception.TodoException;
import com.todo.app.model.Todo;
import com.todo.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("")
    public ResponseEntity<Page<Todo>> getAllTodo(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Page<Todo> todoPage = todoService.getPaginatedTodos(page, size);
        return new ResponseEntity<>(todoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Integer id) throws TodoException {
        return new ResponseEntity<>(todoService.getTodoById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        return new ResponseEntity<>(todoService.createTodo(todo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Integer id, @Valid @RequestBody Todo todo) throws TodoException {
        return new ResponseEntity<>(todoService.updateTodo(id, todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer id) throws TodoException {
        return new ResponseEntity<>(todoService.deleteTodo(id), HttpStatus.OK);
    }
}

