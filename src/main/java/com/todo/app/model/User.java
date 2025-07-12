package com.todo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Useremail cannot be empty")
    @Email(message = "Invalid email format")
    private String useremail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Todo> todos;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        if (todos != null) {
            for (Todo todo : todos) {
                todo.setUser(this);
            }
        }
    }

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User(int userid, String username, String useremail, List<Todo> todos) {
        super();
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.todos = todos;
    }

    @Override
    public String toString() {
        return "User [userid=" + userid + ", username=" + username + ", useremail=" + useremail + ", todos=" + todos
                + "]";
    }
}

