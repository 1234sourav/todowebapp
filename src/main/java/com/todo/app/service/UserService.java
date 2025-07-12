package com.todo.app.service;

import com.todo.app.exception.UserException;
import com.todo.app.model.User;
import com.todo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Integer id) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserException("User Not Found");
        }
    }

    public User createUser(User user) {
        User newUser = userRepository.save(user);
        return newUser;
    }

    public User updateUser(Integer id, User user) throws UserException {
        if (userRepository.findById(id).isPresent()) {
            User newUser = userRepository.findById(id).get();
            newUser.setUsername(user.getUsername());
            newUser.setUseremail(user.getUseremail());
            User updatedUser = userRepository.save(newUser);
            return updatedUser;
        } else {
            throw new UserException("User Not Found");
        }
    }

    public String deleteUser(Integer id) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return "User deleted Successfully";
        } else {
            throw new UserException("User Not Found");
        }
    }
}

