package com.todo.app.repository;

import com.todo.app.model.Todo;
import com.todo.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoRepository extends JpaRepository<Todo, Integer> {
    Page<Todo> findByUser(User user, Pageable pageable);
}







