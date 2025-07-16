package com.todo.app.controller;

import com.todo.app.exception.TodoException;
import com.todo.app.model.Todo;
import com.todo.app.model.User;
import com.todo.app.repository.UserRepository;
import com.todo.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api-todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<Page<Todo>> getAllTodo(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "50") int size,
                                                 Principal principal) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Todo> todoPage = todoService.getPaginatedTodos(username, page, size);
        return new ResponseEntity<>(todoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Integer id, Principal principal) throws TodoException {
        Todo todo = todoService.getTodoById(id);
        if (todo == null || !todo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Unauthorized access or Todo not found");
        }
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        todo.setUser(user);
        return new ResponseEntity<>(todoService.createTodo(todo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Integer id,
                                           @Valid @RequestBody Todo todo,
                                           Principal principal) throws TodoException {
        Todo existingTodo = todoService.getTodoById(id);
        if (existingTodo == null || !existingTodo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Unauthorized update attempt or Todo not found");
        }

        todo.setUser(existingTodo.getUser()); // Keep the original user
        return new ResponseEntity<>(todoService.updateTodo(id, todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer id, Principal principal) throws TodoException {
        Todo todo = todoService.getTodoById(id);
        if (todo == null || !todo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Unauthorized delete attempt or Todo not found");
        }

        return new ResponseEntity<>(todoService.deleteTodo(id), HttpStatus.OK);
    }
}

