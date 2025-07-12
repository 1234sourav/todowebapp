package com.todo.app.service;

import com.todo.app.exception.TodoException;
import com.todo.app.model.Todo;
import com.todo.app.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    TodoRepository todoRepository;

    public Page<Todo> getPaginatedTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    }

    public Todo getTodoById(Integer id) throws TodoException {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            return todoOptional.get();
        } else {
            throw new TodoException("Todo Not Found!");
        }
    }

    public Todo createTodo(Todo todo) {
        Todo t = todoRepository.save(todo);
        return t;
    }

    public Todo updateTodo(int id, Todo todo) throws TodoException {
        if (todoRepository.findById(id).isPresent()) {
            Todo newTodo = todoRepository.findById(id).get();
            newTodo.setTask(todo.getTask());
            newTodo.setStatus(todo.getStatus());
            newTodo.setDueDate(todo.getDueDate());
            Todo updatedTodo = todoRepository.save(newTodo);
            return updatedTodo;
        } else {
            throw new TodoException("Todo Not Found");
        }
    }

    public String deleteTodo(Integer id) throws TodoException {
        Optional<Todo> tudoOptional = todoRepository.findById(id);

        if (tudoOptional.isPresent()) {
            todoRepository.deleteById(id);
            return "Deleted Successfully";
        } else {
            throw new TodoException("Todo Not Found");
        }
    }
}

