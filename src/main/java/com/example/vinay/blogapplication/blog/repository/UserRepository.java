package com.example.vinay.blogapplication.blog.repository;

import com.example.vinay.blogapplication.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    User findByEmail(String email);
}
