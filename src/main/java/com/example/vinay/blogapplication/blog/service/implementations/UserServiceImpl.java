package com.example.vinay.blogapplication.blog.service.implementations;

import com.example.vinay.blogapplication.blog.model.User;
import com.example.vinay.blogapplication.blog.repository.UserRepository;
import com.example.vinay.blogapplication.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void registerUser(User user) {
        String bcryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(bcryptPassword);

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
