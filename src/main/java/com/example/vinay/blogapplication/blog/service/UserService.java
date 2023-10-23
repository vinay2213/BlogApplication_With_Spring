package com.example.vinay.blogapplication.blog.service;

import com.example.vinay.blogapplication.blog.model.User;

public interface UserService {
    User findByName(String name);
    void registerUser(User user);
    User findByEmail(String email);
}
