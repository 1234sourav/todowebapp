package com.todo.app.controller;

import com.todo.app.exception.TodoException;
import com.todo.app.model.Todo;
import com.todo.app.model.User;
import com.todo.app.repository.UserRepository;
import com.todo.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class TodoViewController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/todo")
    public String viewAllTodo(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Principal principal,
                              Model model) {
        String username = principal.getName();
        Page<Todo> todoPage = todoService.getPaginatedTodos(username, page, size);
        model.addAttribute("todoPage", todoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", todoPage.getTotalPages());
        return "todo";
    }

    // Method for creating todos
    @GetMapping("/create")
    public String createTodo(Model model) {
        Todo t = new Todo();
        model.addAttribute("todos", t);
        List<String> status = Arrays.asList("Complete", "Not complete");
        model.addAttribute("status", status);
        return "createTodo";
    }

    // Method for submitting the todos
    @PostMapping("/todo")
    public String submitTodo(Model model,
                             @Valid @ModelAttribute("todos") Todo todo,
                             BindingResult result,
                             Principal principal) {
        if (result.hasErrors()) {
            List<String> status = Arrays.asList("Complete", "Not complete");
            model.addAttribute("status", status);
            return "createTodo";
        }

        User user = userRepository.findByUsername(principal.getName());
        todo.setUser(user);

        todoService.createTodo(todo);
        return "redirect:/todo";
    }

    // Method for updating todos
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model, Principal principal) throws TodoException {
        Todo todo = todoService.getTodoById(id);

        if (todo == null || !todo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Todo not found or unauthorized");
        }
        model.addAttribute("todo", todo);
        List<String> status = Arrays.asList("Complete", "Not complete");
        model.addAttribute("status", status);
        return "updateTodo";
    }

    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Integer id,
                             @Valid @ModelAttribute("todo") Todo todo,
                             BindingResult result,
                             Model model,
                             Principal principal) throws TodoException {
        if (result.hasErrors()) {
            todo.setTodo_id(id);
            model.addAttribute("todo", todo);
            List<String> status = Arrays.asList("Complete", "Not complete");
            model.addAttribute("status", status);
            return "updateTodo";
        }

        Todo existingTodo = todoService.getTodoById(id);
        if (existingTodo == null || !existingTodo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Todo not found or unauthorized");
        }

        todo.setUser(existingTodo.getUser());

        todoService.updateTodo(id, todo);
        return "redirect:/todo";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id, Principal principal) throws TodoException {
        Todo todo = todoService.getTodoById(id);
        if (todo == null || !todo.getUser().getUsername().equals(principal.getName())) {
            throw new TodoException("Todo not found or unauthorized");
        }

        todoService.deleteTodo(id);
        return "redirect:/todo";
    }
}

