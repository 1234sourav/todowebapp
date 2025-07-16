package com.todo.app.service;

import com.todo.app.exception.TodoException;
import com.todo.app.model.Todo;
import com.todo.app.model.User;
import com.todo.app.repository.TodoRepository;
import com.todo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    public Page<Todo> getPaginatedTodos(String username, int page, int size) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findByUser(user, pageable);
    }

    public Todo getTodoById(Integer id) throws TodoException {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoException("Todo Not Found!"));
    }

    public Todo createTodo(Todo todo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(int id, Todo todo) throws TodoException {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoException("Todo Not Found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!existingTodo.getUser().getUsername().equals(username)) {
            throw new TodoException("Access denied: You do not own this todo");
        }

        existingTodo.setTask(todo.getTask());
        existingTodo.setStatus(todo.getStatus());
        existingTodo.setDueDate(todo.getDueDate());

        return todoRepository.save(existingTodo);
    }

    public String deleteTodo(Integer id) throws TodoException {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoException("Todo Not Found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!todo.getUser().getUsername().equals(username)) {
            throw new TodoException("Access denied: You do not own this todo");
        }
        todoRepository.delete(todo);
        return "Deleted Successfully";
    }
}

