package com.service;

import com.model.User;

import java.util.List;

public interface UserService {

    public User loginUser(String username,String password);
    public List<User> getAllUsers();
}
