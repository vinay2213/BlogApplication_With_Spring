package com.example.vinay.blogapplication.blog.service.implementations;

import com.example.vinay.blogapplication.blog.model.User;
import com.example.vinay.blogapplication.blog.repository.UserRepository;
import com.example.vinay.blogapplication.blog.service.UserInfoUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Name Not Found: " + username);
        }

        return new UserInfoUserDetails(user);
    }

}
