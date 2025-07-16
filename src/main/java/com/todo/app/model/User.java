package com.todo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, message = "Username cannot be less than 3 characters")
    private String username;

    @NotEmpty(message = "Useremail cannot be empty")
    @Email(message = "Invalid email format")
    private String useremail;

    @NotEmpty(message = "Userpassword cannot be empty")
    @Size(min = 6, message = "Userpassword must be at least 6 characters long")
    private String userpassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Todo> todos;

    public User(int userid, String username, String useremail, String userpassword, List<Todo> todos) {
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.todos = todos;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

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

