package com.todo.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int todo_id;

    @NotEmpty(message = "Task cannot be empty")
    @Size(max = 255, message = "Task must be less than 255 characters")
    private String task;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Date cannot be null")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Todo(int todo_id, String task, String status, LocalDate dueDate, User user) {
        super();
        this.todo_id = todo_id;
        this.task = task;
        this.status = status;
        this.dueDate = dueDate;
        this.user = user;
    }

    public Todo() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Todo [todo_id=" + todo_id + ", task=" + task + ", status=" + status + ", dueDate=" + dueDate + ", user="
                + user + "]";
    }
}

